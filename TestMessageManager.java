import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Project 4 -> TestMessageManager
 *
 * class handles all the testing of MessageManager
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class TestMessageManager {
    // Useful constants.
    private static final String[] ids = {"PYzww08h9X985c", "ysJKgy0v2hyVY3"};
    private static final String dbPath = "testHistory/databases/db1.txt";
    private static final String histPath = "testHistory/history";
    private static File[] files = new File[2];
    private static ArrayList<String> messageIDs = new ArrayList<String>();
    public static void main(String[] args) {
        // Setup
        for (int i = 0; i < 2; i++) {
            files[i] = new File(histPath + "/" + ids[i] + "-messageHistory.txt");
        }

        System.out.println("Test " + (test0() ? "Passed" : "Failed"));
        System.out.println("Test " + (test1() ? "Passed" : "Failed"));
        System.out.println("Test " + (test2() ? "Passed" : "Failed"));
        System.out.println("Test " + (test3() ? "Passed" : "Failed"));

        // Cleanup to ensure consistent tests.
        for (File f: files) {
            f.delete();
        }
    }
    public static boolean test0() {
        // tests construction.
        try {
            MessageManager mm1 = new MessageManager(new Database(dbPath), histPath);
            for (int i = 0; i < 10; i++) {
                String message = String.format("%d - %d - %d", i, i + 1, i + 2);
                mm1.messageUser(ids[0], ids[1], message, "");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean test1() {
        // tests messageUser.
        try {
            MessageManager mm = new MessageManager(new Database(dbPath), histPath);
            ArrayList<HashMap<String,String>> history = mm.getPersonalHistory(ids[0]);
            ArrayList<String> messages = new ArrayList<String>();
            for (HashMap<String,String> hist : history) {
                if (hist.containsKey("message")) {
                    messages.add(hist.get("message"));
                }
                if (hist.containsKey("messageNum")) {
                    messageIDs.add(hist.get("messageNum"));
                }
            }
            for (int i = 0; i < 10; i++) {
                String message = String.format("%d - %d - %d", i, i + 1, i + 2);
                if (!messages.get(i).equals(message)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean test2() {
        // tests deleteMessage.
        try {
            Database db = new Database(dbPath);
            MessageManager mm1 = new MessageManager(db, histPath);
            ArrayList<String> messages = new ArrayList<String>();
            for (int i = 3; i < 10; i++) {
                mm1.deleteMessage(ids[0], ids[1], messageIDs.get(i));
            }

            MessageManager mm2 = new MessageManager(db, histPath);
            ArrayList<HashMap<String,String>> history = mm2.getPersonalHistory(ids[0]);
            for (HashMap<String,String> hist : history) {
                if (hist.containsKey("message")) {
                    messages.add(hist.get("message"));
                }
            }
            for (int i = 0; i < 3; i++) {
                String message = String.format("%d - %d - %d", i, i + 1, i + 2);
                if (!messages.get(i).equals(message)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean test3() {
        // tests editMessage.
        try {
            Database db = new Database(dbPath);
            MessageManager mm1 = new MessageManager(db, histPath);
            ArrayList<String> messages = new ArrayList<String>();
            for (int i = 0; i < 3; i++) {
                mm1.editMessage(ids[0], ids[1], String.format("%d", i * 10), messageIDs.get(i));
            }

            MessageManager mm2 = new MessageManager(db, histPath);
            ArrayList<HashMap<String,String>> history = mm2.getPersonalHistory(ids[0]);
            for (HashMap<String,String> hist : history) {
                if (hist.containsKey("message")) {
                    messages.add(hist.get("message"));
                }
            }
            for (int i = 0; i < 3; i++) {
                String message = String.format("%d", i * 10);
                if (!messages.get(i).equals(message)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}