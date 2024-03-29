import java.io.File;
import java.util.HashMap;

/**
 * Project 5 -> TestDatabase
 *
 * Testing our database class
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class TestDatabase {;
    private static final String FILEPATH = "testDatabase/db.txt";
    private static File f = new File(FILEPATH);
    public static void main(String[] args) throws Exception {
        String FILEPATH = "testDatabase/db.txt";
        File f = new File(FILEPATH);
        PathManager.storeDir = "testDatabase/";
        System.out.println("Test " + (test1() ? "Passed" : "Failed"));
        System.out.println("Test " + (test2() ? "Passed" : "Failed"));
        System.out.println("Test " + (test3() ? "Passed" : "Failed"));
        System.out.println("Test " + (test4() ? "Passed" : "Failed"));
        f.delete();
    }

    public static boolean test1() throws Exception {
        // Test get.
        Database db1 = new Database();
        try {
            for (int i = 0; i < 10; i++) {
                String username = String.format("User%d@gmail.com", i + 1);
                String password = String.format("password%d", i + 1);
                db1.add(username, password, Role.Customer);
            }
            db1.save();
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return false;
        }

        Database db2 = new Database();
        for (int i = 0; i < 10; i++) {
            boolean valid = false;
            String username = String.format("User%d@gmail.com", i + 1);
            String password = String.format("password%d", i + 1);
            HashMap<String, String> user = db2.get("email", username);
            String loadedPassword = user.get("password");
            valid = password.equals(loadedPassword);
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    public static boolean test2() throws Exception {
        // Test verify.
        Database db = new Database();
        for (int i = 0; i < 10; i++) {
            String username = String.format("User%d@gmail.com", i + 1);
            String password = String.format("password%d", i + 1);
            boolean valid = db.verify(username, password);
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    public static boolean test3() throws Exception {
        // Test modify.
        Database db1 = new Database();
        for (int i = 6; i < 10; i++) {
            String username = String.format("User%d@gmail.com", i + 1);
            String password = String.format("password%d", i + 1);
            try {
                db1.modify(username, "password", password + "edited");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        db1.save();

        Database db2 = new Database();
        for (int i = 6; i < 10; i++) {
            String username = String.format("User%d@gmail.com", i + 1);
            String password = String.format("password%dedited", i + 1);
            boolean valid = db2.verify(username, password);
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    public static boolean test4() throws Exception {
        // Test block.
        Database db1 = new Database();
        for (int i = 0; i < 5; i++) {
            String username1 = String.format("User%d@gmail.com", i + 1);
            String username2 = String.format("User%d@gmail.com", i + 2);
            try {
                db1.block(username1, username2, false);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        db1.save();

        Database db2 = new Database();
        for (int i = 0; i < 5; i++) {
            String username1 = String.format("User%d@gmail.com", i + 1);
            HashMap<String, String> user1 = db2.get("email", username1);
            String[] blocked = user1.get("blocked").split(",");
            String username2 = String.format("User%d@gmail.com", i + 2);
            HashMap<String, String> user2 = db2.get("email", username2);
            boolean valid = blocked[0].equals(user2.get("id"));
            if (!valid) {
                return false;
            }
        }
        return true;
    }
}
