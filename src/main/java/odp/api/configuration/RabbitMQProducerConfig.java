package odp.api.configuration;

import com.rabbitmq.client.ConnectionFactory;
import odp.api.beans.InitBean;
import odp.api.utilities.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLSocketFactory;
import java.io.FileNotFoundException;

@Configuration
public class RabbitMQProducerConfig {
    @Value("${default.amqp.producer.user}")
    public String producerUsername;

    @Value("${default.amqp.producer.password}")
    public String producerPassword;

    @Value("${default.amqp.producer.host}")
    public String producerHost;

    @Value("${default.amqp.producer.port}")
    public Integer producerPort;

    @Value("${default.amqp.producer.ssl_ca}")
    protected String producerCaPathWeb;

    @Value("${default.amqp.producer.ssl_cert}")
    protected String producerCertPathWeb;

    @Value("${default.amqp.producer.ssl_key}")
    protected String producerKeyPathWeb;

    @Value("${default.amqp.producer.ssl_keyPassword}")
    protected String producerKeyPWWeb;

    @Value("${default.amqp.producer.max_channel}")
    protected String producerMaxChannel;

    @Bean
    public ConnectionFactory rabbitProducerConnectionFactory(){
        Logger logger = LoggerFactory.getLogger(RabbitMQProducerConfig.class);
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(producerHost);
        connectionFactory.setPort(producerPort);
        connectionFactory.setUsername(producerUsername);
        connectionFactory.setPassword(producerPassword);
        connectionFactory.setRequestedChannelMax(Integer.parseInt(producerMaxChannel));

        try {
            SSLSocketFactory socketFactory;
            socketFactory = Util.getSocketFactory(producerCaPathWeb, producerCertPathWeb, producerKeyPathWeb, producerKeyPWWeb);
            connectionFactory.setSocketFactory(socketFactory);
            InitBean.getInstance().initializeMapping("producerConfig");
        } catch (FileNotFoundException e){
            logger.info("File Error: " + e.getMessage());
        } catch (Exception e) {
            logger.info(e.toString());
            logger.info("Unable to setup rabbit mq client with ssl connection");
        }

        return connectionFactory;
    }
}
