package odp.api.configuration;

import org.apache.camel.component.http.HttpClientConfigurer;
import odp.api.utilities.SelfSignedHttpClientConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

    @Bean(name = "skipSSL")
    public HttpClientConfigurer skipSSL(){
        SelfSignedHttpClientConfigurer config = new SelfSignedHttpClientConfigurer();
        return config;
    }
}
