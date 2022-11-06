import java.io.*;
import java.util.*;
import java.time.*;
/**
 * This class handles message sending for a particular user.
 */
public class MessageManager {
    private Database db;
    public MessageManager() {
        db = new Database("UserDatabase.txt");
    }

    //Returns the names of all the customers a user can talk to
    public ArrayList<String> getNames(String type) throws InvalidUserException {
        ArrayList<HashMap<String, String>> results = db.getSelection("role", type);
        ArrayList<String> names = new ArrayList<String>();
        for (HashMap<String, String> result: results) {
            names.add(result.get("username"));
        }
        return names;
    }

    public void messageUser(String username, String usernameToSendMessageTo, String message) {
        //TODO: Implement messageUser
    }

    public void editMessage(String username, String usernameToSendMessageTo, String newMessage) {

    }
    public void deleteMessage(String username, String usernameToSendMessageTo, String message) {

    }

}