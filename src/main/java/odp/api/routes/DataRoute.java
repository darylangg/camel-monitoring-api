package odp.api.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DataRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("rabbitmq:daryl_test?queue=daryl_test&connectionFactory=#rabbitConsumerConnectionFactory")
            .to("direct:lol");
    }
}
