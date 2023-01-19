package odp.api.beans;

import org.springframework.stereotype.Component;
import odp.api.types.ResponseType;

@Component
public class GetBean {

    public ResponseType sayHello() {
        // Your logic can go here! If you return a POJO, Camel will try and serialise it to JSON.
        return new ResponseType("Hello, world!");
    }

}
