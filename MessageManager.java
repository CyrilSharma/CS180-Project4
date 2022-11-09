import java.io.*;
import java.time.format.DateTimeFormatter;
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
        int messageCount;
        String messageSplit = "-----";
        LocalTime time = LocalTime.now();
        DateTimeFormatter myFormatTime = DateTimeFormatter.ofPattern(("MM-dd-yyyy HH:mm"));
        //timestamp = myFormatTime variable
        boolean previousLine = false;
        boolean convExists = false;
        File f = new File("messageHistory.txt");
        ArrayList<String> history = new ArrayList<String>();
        if (f.createNewFile() || f.length() == 0) {
            //messageHistory does not exist
            FileOutputStream fos = new FileOutputStream(f, false);
            PrintWriter pw1 = new PrintWriter(fos);
            messageCount = 1;
            pw1.println(db.getSelection("role", "Customer") + "-" + db.getSelection("role", "Seller"));
            pw1.println(messageCount + "<" + myFormatTime + ">" + sender + ": " + message);
            pw1.println(messageSplit);
            fos.close();
            pw1.close();
        } else {
            BufferedReader br = new BufferedReader(new FileReader(f));
            PrintWriter pw = new PrintWriter(new FileWriter(f), false);
            String line = br.readLine();
            while (line != null) {
                history.add(line);
                br.readLine();
            }
            for (int i = 0; i < history.size(); i++) {
                if (history.contains(sender + "-" + recipient) || history.contains(recipient + "-" + sender)) {
                    //conversation exists
                    while (!previousLine) {
                        if (history.get(i).equals(messageSplit)) {
                            messageCount = i - 1;
                            previousLine = true;
                            // need to reprint file with new line replacing dashes(messageSplit), dashes line below
                            //followed by a blank line then next conversation
                            //
                        }
                    }

                }
            }
            pw.close();
            br.close();
        }

    }

    public void editMessage(String username, String usernameToSendMessageTo, int messageNum, String newMessage) throws IOException {
        File f = new File("messageHistory.txt");
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
        pw.close();
    }

    public void deleteMessage(String username, String usernameToSendMessageTo, String message) throws IOException {
        File f = new File("messageHistory.txt");
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
