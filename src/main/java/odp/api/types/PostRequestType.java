package odp.api.types;

/**
 * This is a POJO which defines a response format.
 * This allows Camel to serialise the response into JSON, using the Jackson library.
 */
public class PostRequestType {

    String name;
    String job;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
