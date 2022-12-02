import java.io.Serializable;

public class Query implements Serializable {
    private String object;
    private String function;
    private Object[] args;

    public Query(String object, String function) {
        this(object, function, new String[]{});
    }

    public Query(String object, String function, String args) {
        this(object, function, new String[]{args});
    }

    public Query(String object, String function, Object[] args) {
        this.object = object;
        this.function = function;
        this.args = args;
    }

    public String getObject() {
        return this.object;
    }

    public String getFunction() {
        return this.function;
    }

    public Object[] getArgs() {
        return this.args;
    }
}
