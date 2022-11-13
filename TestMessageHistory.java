import java.util.*;
/**
 * Project 4 ->TestMessageHistory
 *
 * class handles the testing of message history
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class TestMessageHistory {
    public static void main(String[] args) {
        Database db = new Database("UserDatabase.txt");
        MessageManager mm = new MessageManager(db, "history");
        //MessageInterface.message(new Scanner(System.in), mm, db, "jkxjksksk");
        MessageInterface.viewMessageHistory(new Scanner(System.in), "jkxjksksk", db, mm);
    }
}
