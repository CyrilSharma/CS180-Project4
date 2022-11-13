import java.util.*;

public class TestMessageHistory {
    public static void main(String[] args) {
        Database db = new Database("UserDatabase.txt");
        MessageManager mm = new MessageManager(db, "history");
        //MessageInterface.message(new Scanner(System.in), mm, db, "jkxjksksk");
        MessageInterface.viewMessageHistory(new Scanner(System.in), "jkxjksksk", db, mm, false, null);
    }
}
