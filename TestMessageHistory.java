import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestMessageHistory {
    public static void main(String[] args) {
        System.out.println("Test " + (test1() ? "Passed" : "Failed"));
    }

    public static boolean test1() {
        MessageManager mm = new MessageManager("testHistory/databases/db1.txt", 
            "testHistory/databases/history");

        try {
            ArrayList<HashMap<String, String>> history = mm.messageUser("jkxjksksk");
            // System.out.println(history);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}