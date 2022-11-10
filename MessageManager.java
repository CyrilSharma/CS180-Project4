import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;
/**
 * This class handles message sending for a particular user.
 */
public class MessageManager {
    private Database db;
    public MessageManager(String path) {
        db = new Database(path);
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
        BufferedReader br = new BufferedReader(new FileReader(username + "messageHistory.txt"));
        PrintWriter pw = new PrintWriter(new FileWriter(username + "messageHistory.txt"), false);
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
        String messageSplit = "-----";
        String senderID = String.valueOf(db.get("id", sender));
        String recipientID = String.valueOf(db.get("id", recipient));
        File fs = new File(senderID + "-messageHistory.txt"); //format: 1231-messageHistory.txt
        File fr = new File(recipientID + "-messageHistory.txt"); //format: 1231-messageHistory.txt
        File[] files = {fs, fr};
        for (File f: files) {
            ArrayList<String> history = new ArrayList<String>();
            if (f.createNewFile()) {
                history.add(senderID + "-" + recipientID);
            } else {
                BufferedReader br = new BufferedReader(new FileReader(f));
                PrintWriter pw = new PrintWriter(new FileWriter(f), false);
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.equals(sender + "-" + recipient) || line.equals(recipient + "-" + sender)) {
                        history.add(line);
                        history.add(message + messageSplit);
                    }
                }
                pw.close();
                br.close();
            }
        }
    }

    public void editMessage(String username, String usernameToSendMessageTo, int messageNum, String newMessage) throws IOException {
        //need to incorproate within both files

        String senderID = String.valueOf(db.getSelection("id", username));
        String recipientID = String.valueOf(db.getSelection("id", usernameToSendMessageTo));


        File f = new File(senderID + "-messageHistory.txt"); //format: 1231-messageHistory.txt
        File f2 = new File(recipientID + "-messageHistory.txt"); //format: 1231-messageHistory.txt


        //File f = new File("messageHistory.txt");
        ArrayList<String> history = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(f));
        PrintWriter pw = new PrintWriter(new FileWriter(f), false);
        String line = br.readLine();
        while (line != null) {
            history.add(line);
            br.readLine();
        }
        for (int i = 0; i < history.size(); i++) {
            if ((i + 1) != messageNum) {
                pw.println(history.get(i));
            } else {
                pw.println(newMessage);
            }
        }
        history.clear();

        BufferedReader br2 = new BufferedReader(new FileReader(f2));
        PrintWriter pw2 = new PrintWriter(new FileWriter(f2), false);
        String line2 = br.readLine();
        while (line2 != null) {
            history.add(line2);
            br.readLine();
        }
        for (int i = 0; i < history.size(); i++) {
            if ((i + 1) != messageNum) {
                pw.println(history.get(i));
            } else {
                pw.println(newMessage);
            }
        }
        br.close();
        pw.close();
        br2.close();
        pw2.close();
    }

    public void deleteMessage(String username, String usernameToSendMessageTo, String message) throws IOException {
        //need to incorporate within a specific file

        String senderID = String.valueOf(db.getSelection("id", username));
        File f = new File(senderID + "-messageHistory.txt"); //format: 1231-messageHistory.txt
        ArrayList<String> history = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(f));
        PrintWriter pw = new PrintWriter(new FileWriter(f), false);
        String line = br.readLine();
        while (line != null) {
            history.add(line);
            br.readLine();
        }
        for (int i = 0; i < history.size(); i++) {
            if (!history.get(i).equals(message)) {
                pw.println(history.get(i));
            } else {
                pw.println("Message deleted");
            }
        }
        pw.close();
    }

}
