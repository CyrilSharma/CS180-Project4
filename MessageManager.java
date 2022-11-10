import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.time.*;
import java.time.Instant;
/**
 * This class handles message sending for a particular user.
 */
public class MessageManager {
    String tokenSep = "|||||";
    String messageSplit = "-----";
    String conversationSplit = "\n#####";
    private Random random;
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

    public void messageUser(String senderID, String recipientID, String message) {
        try {
            generalMessage(senderID, recipientID, message, "message", -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editMessage(String senderID, String recipientID, String message, String messageId) {
        try {
            generalMessage(senderID, recipientID, message, "edit", messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(String senderID, String recipientID, String messageId) {
        try {
            generalMessage(senderID, recipientID, "", "delete", messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void generalMessage(String senderID, String recipientID, String message, String action, String messageID) throws IOException {
        File[] files = {new File(senderID + "-messageHistory.txt"),
            new File(recipientID + "-messageHistory.txt")};

        for (int fileInd = 0; fileInd < files.length; fileInd++) {
            // don't update recipient file on delete.
            if (action.equals("delete") && fileInd == 1) {
                continue;
            }
            File f = files[fileInd];
            ArrayList<String> history = new ArrayList<String>();
            if (f.createNewFile()) {
                history.add(senderID + "-" + recipientID);
            } else {
                Scanner scan = new Scanner(f);
                PrintWriter pw = new PrintWriter(new FileWriter(f), false);
                String line;
                int messageLine = 0;
                int counter = 0;

                // read until you find the conversation.
                String curLine = "";
                while ((curLine = scan.nextLine()) != null) {
                    history.add(curLine);
                    if (!curLine.equals(senderID + "-" + recipientID)) {
                        break;
                    }
                }

                boolean conversationOver = false;
                HashSet<String> hm = new HashSet<String>();
                while (scan.hasNextLine() && !conversationOver) {
                    // read potentially multiline string.
                    line = "";
                    String diff;
                    while (!(diff = scan.nextLine()).contains(messageSplit)) {
                        line += diff;
                        if (diff.equals(conversationSplit)) {
                            conversationOver = true;
                            break;
                        }
                    }

                    // read lines in conversation, and determine postion to insert | delete
                    // also keep track of which ids exist.
                    history.add(line);
                    String[] tokens = line.split(tokenSep);
                    String id = tokens[tokens.length-1];
                    if (action.equals("message")) {
                        if (line.equals(senderID + "-" + recipientID) || line.equals(recipientID + "-" + senderID)) {
                            messageLine = counter;
                        }
                    } else if (action.equals("modify") || action.equals("delete")) {
                        if (id == messageID) {
                            messageLine = counter;
                        }
                    }
                    hm.add(id);
                    counter++;
                }

                // finish reading the file.
                curLine = "";
                while ((curLine = scan.nextLine()) != null) {
                    history.add(curLine);
                }

                // find valid new message ID.
                String newId = "";
                do {
                    for (int i = 0; i < 14; i++) {
                        int num = random.nextInt(50);
                        newId += (char)('0' + num);
                    }
                } while (hm.contains(newId));

                // add new message along with associated information
                if (action.equals("message")) {
                    String time = Instant.now().toString();
                    history.add(messageLine, message + tokenSep + senderID + tokenSep 
                        + newId + tokenSep + time + messageSplit);
                } else if (action.equals("modify")) {
                    String time = Instant.now().toString();
                    history.add(messageLine, message + tokenSep + senderID + tokenSep 
                        + newId + tokenSep + time + messageSplit);
                } else if (action.equals("delete")) {
                    history.remove(messageLine);
                }
                
                // add conversation delimiter if it does not exist.
                if (!history.get(history.size() - 1).equals(conversationSplit)) {
                    history.add(conversationSplit);
                }
                pw.close();
                scan.close();
            }
        }
    }
}
