package odp.api.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataRoute extends RouteBuilder {
    private static final Logger log = LogManager.getLogger(DataRoute.class);

    @Value("${default.verticals}")
    private String[] verticalList;

    @Override
    public void configure() throws Exception {
        for (String queue : verticalList) {
            from("rabbitmq:daryl_test?queue=daryl_"+queue+"&connectionFactory=#rabbitConsumerConnectionFactory")
                .routeId(queue)
                .to("direct:lol");
        }
    }
}
