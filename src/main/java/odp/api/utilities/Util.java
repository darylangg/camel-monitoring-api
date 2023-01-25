package odp.api.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Util {
	private static final Logger logger = LogManager.getLogger(Util.class);

	private Util() {
		throw new IllegalStateException("Utility class");
	}

	public static SSLSocketFactory getSocketFactory(final String caCrtFile,
													final String crtFile, final String keyFile, final String password)
			throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
		Security.addProvider(new BouncyCastleProvider());

		FileInputStream fis = new FileInputStream(caCrtFile);

		X509Certificate cert = null;
		X509Certificate caCert = null;

		// read ca cert
		try (BufferedInputStream bis = new BufferedInputStream(fis)){
			CertificateFactory cf = CertificateFactory.getInstance("X.509");

			while (bis.available() > 0) {
				caCert = (X509Certificate) cf.generateCertificate(bis);
			}
		} catch (Exception e) {
			logger.info(e.toString());
		}

		// read client cert
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(crtFile))){
			CertificateFactory cf = CertificateFactory.getInstance("X.509");

			// load client certificate
			while (bis.available() > 0) {
				cert = (X509Certificate) cf.generateCertificate(bis);
			}
		} catch (Exception e) {
			logger.info(e.toString());
		}

		// load client private key
		PEMParser pemParser = null;
		KeyPair key = null;
		try (FileReader fr = new FileReader(keyFile)){
			pemParser = new PEMParser(fr);

			Object object = pemParser.readObject();
			PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder()
					.build(password.toCharArray());
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
					.setProvider("BC");
			if (object instanceof PEMEncryptedKeyPair) {
				logger.info("Encrypted key - we will use provided password");
				key = converter.getKeyPair(((PEMEncryptedKeyPair) object)
						.decryptKeyPair(decProv));
			} else {
				logger.info("Unencrypted key - no password needed");
				key = converter.getKeyPair((PEMKeyPair) object);
			}
		} catch (Exception e) {
			logger.info(e);
		} finally {
			if (pemParser != null){
				pemParser.close();
			}
		}

		// CA certificate is used to authenticate server
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", caCert);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init(caKs);

		// client key and certificates are sent to server so it can authenticate
		// us
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", cert);
		if (key != null){
			ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
					new java.security.cert.Certificate[] { cert });
		}
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
				.getDefaultAlgorithm());
		kmf.init(ks, password.toCharArray());

		// finally, create SSL socket factory
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return context.getSocketFactory();
	}
}
