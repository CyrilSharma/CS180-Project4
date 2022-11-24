public class Query {
    private String object;
    private String function;
    private String[] args;

    public Query(String object, String function, String args) {
        this(object, function, new String[]{args});
    }

    public Query(String object, String function, String[] args) {
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

    public String[] getArgs() {
        return this.args;
    }
}
