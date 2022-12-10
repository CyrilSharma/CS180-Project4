import java.io.Serializable;
/**
 * Project 5 -> Query
 *
 * This class handles all the queries that are being sent
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
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
    /**
     * returns the object
     *
     * @return object
     */
    public String getObject() {
        return this.object;
    }
    /**
     * returns the function
     *
     * @return String function
     */
    public String getFunction() {
        return this.function;
    }
    /**
     * returns the arguments
     *
     * @return arguments
     */
    public Object[] getArgs() {
        return this.args;
    }
}
