import java.io.File;
import java.util.HashMap;
/**
 * This class contains tests and some utility functions for making test data.
 */
public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("Test " + (test1() ? "Passed" : "Failed"));
    }

    public static boolean test1() {
        // Save and Load database.
        String filepath = "testDatabase/testDatabase1.txt";

        // Always start with a blank file.
        File f = new File(filepath);
        f.delete();

        Database db1 = new Database(filepath);
        try {
            for (int i = 0; i < 10; i++) {
                String username = String.format("User%d@gmail.com", i + 1);
                String password = String.format("password%d", i + 1);
                db1.add(username, password, Role.Customer);
            }
            db1.save();
        } catch (InvalidUserException e) {
            e.printStackTrace();
            f.delete();
            return false;
        }

        Database db2 = new Database(filepath);
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
        f.delete();
        return true;
    }
}
