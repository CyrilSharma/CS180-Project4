import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterInterfaceGUI {
    private Translator tr;
    private static boolean status;
    public FilterInterfaceGUI() {
        tr = new Translator();
    }
    public ArrayList<String> getWords() {
        ArrayList<String> words = new ArrayList<>();
        try {
             words = (ArrayList<String>) tr.query(new Query("Filter", "get"));
        } catch (Exception e) {

        }
        return words;
    }
    public void addWord(String word) {
        try {
            String[] param = {word};
            tr.query(new Query("Filter", "add", param));
        } catch (Exception e) {

        }
    }

    public void removeWord(String word) {
        try {
            String[] param = {word};
            tr.query(new Query("Filter", "remove", param));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void changeStatus(boolean stat) {
        status = stat;
    }

    public static boolean status() {
        return status;
    }

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
