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

    public void messageUser(String username, String usernameToSendMessageTo, String message) throws IOException {
        //TODO: Implement messageUser
        BufferedReader customerReader = new BufferedReader(new FileReader("messageHistoryCustomer.txt"));
        BufferedReader sellerReader = new BufferedReader(new FileReader("messageHistorySeller.txt"));
        PrintWriter customerWriter = new PrintWriter(new FileWriter("messageHistoryCustomer.txt"), false);
        PrintWriter sellerWriter = new PrintWriter(new FileWriter("messageHistorySeller.txt"), false);
//        ArrayList<String> fileContents = new ArrayList<>();
//
//        String line = customerReader.readLine();
//        while (true) {
//            fileContents.add(line);
//            if (line == null) {
//                break;
//            }
//            line = customerReader.readLine();
//        }
//        boolean exists = false;
//        for (String messageExists : fileContents) {
//            if (messageExists.contains(username + "-" + usernameToSendMessageTo)) {
//                exists = true;
//            }
//        }
//        customerWriter.println(username + "-" + usernameToSendMessageTo);


    }

    public void editMessage(String username, String usernameToSendMessageTo, String newMessage) {
        //TODO

    }
    public void deleteMessage(String username, String usernameToSendMessageTo, String message) {
        //TODO
    }

}