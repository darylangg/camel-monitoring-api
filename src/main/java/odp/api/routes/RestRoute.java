package odp.api.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import odp.api.types.PostRequestType;
import odp.api.types.ResponseType;

/**
 * This RouteBuilder defines our REST API using Camel's REST DSL.
 *
 * A RestConfiguration block first defines how the service will be instantiated.
 * The rest() DSL block then defines each of the RESTful service operations.
 */
@Component
public class RestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.auto);
        rest()
            .path("/api") // This makes the API available at http://host:port/$CONTEXT_ROOT/api

            .consumes("application/json")
            .produces("application/json")

            // HTTP: GET /api
            .get()
                .outType(ResponseType.class) // Setting the response type enables Camel to marshal the response to JSON
                .to("bean:getBean") // This will invoke the Spring bean 'getBean'

            // HTTP: POST /api
            .post()
                .type(PostRequestType.class) // Setting the request type enables Camel to unmarshal the request to a Java object
                .outType(ResponseType.class) // Setting the response type enables Camel to marshal the response to JSON
                .to("bean:postBean");

    }
}
