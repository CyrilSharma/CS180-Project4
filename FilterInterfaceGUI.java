import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Project 5 -> FilterInterfaceGUI
 *
 * FilterIntererface that connects to the Filter GUI
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class FilterInterfaceGUI {
    private Translator tr;
    private static boolean status;
    public FilterInterfaceGUI() {
        tr = new Translator();
    }

    /**
     * returns the words that are filtered
     *
     * @return ArrayList<String> words</String>
     */
    public ArrayList<String> getWords() {
        ArrayList<String> words = new ArrayList<>();
        try {
             words = (ArrayList<String>) tr.query(new Query("Filter", "get"));
        } catch (Exception e) {

        }
        return words;
    }
    /**
     * adds a word to the query
     *
     * @param word
     */
    public void addWord(String word) {
        try {
            String[] param = {word};
            tr.query(new Query("Filter", "add", param));
        } catch (Exception e) {

        }
    }
    /**
     * removes a word to the query
     *
     * @param word
     */
    public void removeWord(String word) throws InvalidWordException {
        try {
            String[] param = {word};
            tr.query(new Query("Filter", "remove", param));
        } catch (Exception e) {
            throw new InvalidWordException("No such word!");
        }
    }
    /**
     * changes the status of the boolean instance field variable
     *
     * @param stat
     */
    public static void changeStatus(boolean stat) {
        status = stat;
    }
    /**
     * returns the status instance variable
     *
     * @return status
     */
    public static boolean status() {
        return status;
    }
    /**
     * filters the message
     *
     * @param msg
     * @return String filteredMessage
     */
    public static String filterMsg(String msg) {
        Translator tr = new Translator();
        try {
            String[] param = {msg};
            return (String) tr.query(new Query("Filter", "filter", param));
        } catch (Exception e) {
        }
        return "";
    }


}
