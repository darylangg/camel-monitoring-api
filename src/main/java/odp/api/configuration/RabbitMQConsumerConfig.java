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
public class RabbitMQConsumerConfig {
    @Value("${default.amqp.consumer.user}")
    public String consumerUsername;

    @Value("${default.amqp.consumer.password}")
    public String consumerPassword;

    @Value("${default.amqp.consumer.host}")
    public String consumerHost;

    @Value("${default.amqp.consumer.port}")
    public Integer consumerPort;

    @Value("${default.amqp.consumer.ssl_ca}")
    protected String consumerCaPathWeb;

    @Value("${default.amqp.consumer.ssl_cert}")
    protected String consumerCertPathWeb;

    @Value("${default.amqp.consumer.ssl_key}")
    protected String consumerKeyPathWeb;

    @Value("${default.amqp.consumer.ssl_keyPassword}")
    protected String consumerKeyPWWeb;

    @Value("${default.amqp.consumer.max_channel}")
    protected String consumerMaxChannel;

    @Bean
    public ConnectionFactory rabbitConsumerConnectionFactory(){
        Logger logger = LoggerFactory.getLogger(RabbitMQConsumerConfig.class);
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(consumerHost);
        connectionFactory.setPort(consumerPort);
        connectionFactory.setUsername(consumerUsername);
        connectionFactory.setPassword(consumerPassword);
        connectionFactory.setRequestedChannelMax(Integer.parseInt(consumerMaxChannel));

        try {
            SSLSocketFactory socketFactory;
            socketFactory = Util.getSocketFactory(consumerCaPathWeb, consumerCertPathWeb, consumerKeyPathWeb, consumerKeyPWWeb);
            connectionFactory.setSocketFactory(socketFactory);
            InitBean.getInstance().initializeMapping("consumerConfig");
        } catch (FileNotFoundException e){
            logger.info("File Error: " + e.getMessage());
        } catch (Exception e) {
            logger.info(e.toString());
            logger.info("Unable to setup rabbit mq client with ssl connection");
        }

        return connectionFactory;
    }
}
