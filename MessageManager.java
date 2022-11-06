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
        ArrayList<String> names = new ArrayList<String>();
        // TODO: reimplement this, maybe make a filter function on the databse?
        return null;
    }

    public void messageUser(String username, String usernameToSendMessageTo, String message) {
        //TODO: Implement messageUser
    }
}