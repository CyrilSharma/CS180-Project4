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
    String conversationSplit = "#####";
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

    public void editMessage(String senderID, String recipientID, String message, int messageId) {
        try {
            generalMessage(senderID, recipientID, message, "edit", messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(String senderID, String recipientID, int messageId) {
        try {
            generalMessage(senderID, recipientID, "", "delete", messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void generalMessage(String senderID, String recipientID, String message, String action, int messageID) throws IOException {
        File[] files = {new File(senderID + "-messageHistory.txt"),
            new File(recipientID + "-messageHistory.txt")};

        for (int i = 0; i < files.length; i++) {
            // don't update recipient file on delete.
            if (action.equals("delete") && i == 1) {
                continue;
            }
            File f = files[i];
            ArrayList<String> history = new ArrayList<String>();
            if (f.createNewFile()) {
                history.add(senderID + "-" + recipientID);
            } else {
                Scanner scan = new Scanner(f);
                scan.useDelimiter(Pattern.compile(messageSplit));
                PrintWriter pw = new PrintWriter(new FileWriter(f), false);
                String line;
                int messageLine = 0;
                int counter = 0;

                // read until you find the conversation.
                String curLine = "";
                while ((curLine = scan.nextLine()) != null) {
                    if (!curLine.equals(senderID + "-" + recipientID)) {
                        history.add(curLine);
                    }
                }

                HashMap<Long, Integer> hm = new HashMap<Long, Integer>();
                while (scan.hasNext()) {
                    line = scan.next();
                    history.add(line);
                    String[] tokens = line.split(tokenSep);
                    Long id = Long.parseLong(tokens[tokens.length-1]);
                    if (action.equals("message")) {
                        messageLine = counter;
                        if (line.equals(senderID + "-" + recipientID) || line.equals(recipientID + "-" + senderID)) {
                            messageLine = counter;
                        }
                    } else if (action.equals("modify") || action.equals("delete")) {
                        if (Integer.parseInt(tokens[1]) == messageID) {
                            messageLine = counter;
                        }
                    }
                    hm.put(id, counter);
                    counter++;
                }

                // find smallest unused ID.
                Long newId = (long) 1;
                while (!hm.containsKey(newId)) {
                    newId++;
                }

                if (action.equals("message")) {
                    String time = Instant.now().toString();
                    history.add(messageLine, message + tokenSep + newId + tokenSep + time + messageSplit);
                } else if (action.equals("modify")) {
                    String time = Instant.now().toString();
                    history.add(messageLine, message + tokenSep + newId + tokenSep + time + messageSplit);
                } else if (action.equals("delete")) {
                    history.remove(messageLine);
                }

                if (!history.get(history.size() - 1).equals(conversationSplit)) {
                    history.add(conversationSplit);
                }
                pw.close();
                scan.close();
            }
        }
    }
}
