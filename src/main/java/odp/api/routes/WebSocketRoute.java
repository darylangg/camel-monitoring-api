package odp.api.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class WebSocketRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        String uri = "websocket://localhost:8443/test?sendToAll=true";

        from("timer:foo?fixedRate=true&period=5000")
            .setBody().constant(">> Welcome on board!")
            .to(uri);
    }
}
