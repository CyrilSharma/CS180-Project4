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
    public void getPersonalHistory(String username) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("messageHistory.txt"));
        PrintWriter pw = new PrintWriter(new FileWriter("messageHistory.txt"), false);
        ArrayList<String> history = new ArrayList<String>();
        ArrayList<String> personal = new ArrayList<String>();
        String line = br.readLine();
        while (line != null) {
            history.add(line);
            br.readLine();
        }
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).contains(username)) {
                ///need to figure out how to grab entire (multiline) message
            }
        }
    }
    public void messageUser(String sender, String recipient, String message) throws IOException {
        //TODO: Implement messageUser
        BufferedReader br = new BufferedReader(new FileReader("messageHistory.txt"));
        PrintWriter pw = new PrintWriter(new FileWriter("messageHistory.txt"), false);
        ArrayList<String> history = new ArrayList<String>();
        String line = br.readLine();
        while (line != null) {
            history.add(line);
            br.readLine();
        }

    }

    public void editMessage(String username, String usernameToSendMessageTo, int messageNum, String newMessage) {
        //TODO
        //find message in txt file
        //replace with newMessage

    }
    public void deleteMessage(String username, String usernameToSendMessageTo, String message) {
        //TODO
        //find message in the txt file
        //replace message with "Message deleted"
    }

}