import java.io.*;
import java.util.*;
import java.time.Instant;
/**
 * Project 4 -> MessageManager
 *
 * This class handles message sending for a particular user. It specifically adds create, edit, and remove message
 * functionality as well as the means to export conversations into a .CSV file.
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class MessageManager {
    String tokenSep = "|||||";
    String messageSplit = "-----";
    String conversationSplit = "#####";
    private Random random;
    private Database db;
    private String historyDir;

    public MessageManager(Database db, String historyPath) {
        this.db = db;
        historyDir = historyPath;
        random = new Random();
    }
    /**
     * Returns the names of all the customers a user can talk to
     */
    public ArrayList<String> getNames(String type) throws InvalidUserException {
        ArrayList<HashMap<String, String>> results = db.getSelection("role", type);
        ArrayList<String> names = new ArrayList<String>();
        for (HashMap<String, String> result: results) {
            names.add(result.get("email"));
        }
        return names;
    }
    /**
     * Returns the personal message history of the user
     */
    public ArrayList<HashMap<String, String>> getPersonalHistory(String id) throws IOException {
        File f = new File(historyDir + "/" + id + "-messageHistory.txt");
        f.createNewFile();
        try (BufferedReader bfr = new BufferedReader(new FileReader(f))) {
            ArrayList<HashMap<String, String>> history = new ArrayList<HashMap<String, String>>();
            String line;
            String recipient = "";
            while ((line = bfr.readLine()) != null) {
                HashMap<String, String> message = new HashMap<String, String>();
                if (!line.contains(tokenSep) && !line.contains(conversationSplit)) {
                    message.put("recipient", line);
                    recipient = line;
                } else if (line.contains(conversationSplit)) {
                    message.put("messageBreak", line);
                } else {
                    String[] lineArray = line.split("\\|\\|\\|\\|\\|");
                    message.put("message", lineArray[0]);
                    message.put("messageNum", lineArray[2]);
                    message.put("recipient", lineArray[1]);
                    //checks whether the person the text file is contacting sent or recieved the message
                    if (message.get("recipient").equals(id)) {
                        message.put("sender", recipient);
                    } else {
                        message.put("sender", id);
                    }
                    message.put("timeStamp", lineArray[3]);
                    message.put("store", lineArray[4].split("-----")[0]);
                }
                history.add(message);
            }
            return history;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Allows the user to message other users (Customers to Sellers and vv, NOT Customers to Customers or Sellers to Sellers
     */
    public void messageUser(String senderID, String recipientID, String message, String store) {
        try {
            generalMessage(senderID, recipientID, message, "message", "", store);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Allows the user to edit a message in an existing conversation
     */
    public void editMessage(String senderID, String recipientID, String message, String messageId) {
        try {
            generalMessage(senderID, recipientID, message, "edit", messageId, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Allows the user to delete a message from THEIR personal message history
     */
    public void deleteMessage(String senderID, String recipientID, String messageId) {
        try {
            generalMessage(senderID, recipientID, "", "delete", messageId, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates general message format/syntax setup used by create, edit, and deleteMessage methods
     */
    public void generalMessage(String senderID, String recipientID, String message, String action, String messageID, String store) throws IOException {
        String[] ids = {senderID, recipientID};
        for (String id: ids) {
            ArrayList<HashMap<String, String>> history = getPersonalHistory(id);
            if (action.equals("message")) {
                if (messageID == null || messageID.equals("")) {
                    do {
                        messageID = "";
                        //TODO: Add do-while loop to this
                        for (int i = 0; i < 14; i++) {
                            int num = random.nextInt(50);
                            messageID += (char)('0' + num);
                        }
                        // lookup ID efficiently.
                    } while (false);
                }
                HashMap<String, String> historyLoc = new HashMap<String, String>();
                historyLoc.put("recipient", id.equals(senderID) ? recipientID : senderID);
                int startLocation = history.indexOf(historyLoc);


                HashMap<String, String> newEntry = new HashMap<String, String>();
                newEntry.put("message", message);
                newEntry.put("recipient", recipientID);
                newEntry.put("messageNum", messageID);
                newEntry.put("timeStamp", Instant.now().toString());
                newEntry.put("store", store);

                int i = startLocation;
                if (startLocation == -1) {
                    HashMap<String, String> temp = new HashMap<String, String>();
                    temp.put("recipient", id.equals(senderID) ? recipientID : senderID);
                    history.add(temp);
                    HashMap<String, String> temp2 = new HashMap<String, String>();
                    temp2.put("messageBreak", conversationSplit);
                    history.add(temp2);
                    i = history.size() - 1;
                } else {
                    HashMap<String, String> line = history.get(i);
                    while (!line.containsKey("messageBreak")) {
                        i++;
                        line = history.get(i);
                    }
                }
                history.add(i, newEntry);
            } else if (action.equals("edit")) {
                for (HashMap<String, String> hist : history) {
                    if (hist.containsKey("messageNum") && hist.get("messageNum").equals(messageID)) {
                        hist.put("message", message);
                        break;
                    }
                }
            } else if (action.equals("delete")){
                if (id.equals(senderID)) {
                    for (HashMap<String, String> hist : history) {
                        if (hist.containsKey("messageNum") && hist.get("messageNum").equals(messageID)) {
                            history.remove(hist);
                            break;
                        }
                    }
                }
            }
            File f = new File(historyDir + "/" + id + "-messageHistory.txt");
            PrintWriter pw = new PrintWriter(f);
            pw.write(formatMessages(history));
            pw.flush();
            pw.close();
        }
    }
    /**
     * Formats message to put into personal message history file
     */
    private String formatMessages(ArrayList<HashMap<String, String>> messages) {
        String messageString = "";
        for (HashMap<String, String> things : messages) {
            if (things.size() > 1) {
                messageString += String.join(tokenSep, things.get("message"), things.get("recipient"), things.get("messageNum"), things.get("timeStamp"), things.get("store") + messageSplit + "\n");
            } else if (things.containsKey("recipient")) {
                messageString += things.get("recipient") + "\n";
            } else {
                messageString += things.get("messageBreak") + "\n";
            }
        }
        return messageString.strip();
    }
    /**
     * Takes a text file and returns the contents as a string, allowing for the contents to be sent to another user
     */
    public String[] readTextFromFile(String path) throws FileNotFoundException {
        String[] text = null;
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File(path)))) {
            ArrayList<String> list = new ArrayList<String>();
            String line;
            while ((line = bfr.readLine()) != null) {
                list.add(line);
            }
            text = new String[list.size()];
            text = list.toArray(text);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
    /**
     * Converts personal message history text file into a CSV file that can be exported
     */
    public void messagesToCSV(String id, String[] idsOfConversationsToRetrieve) throws IOException {
        ArrayList<HashMap<String, String>> history = getPersonalHistory(id);
        String text = "Message ID, Sender,Recipient,Store,Time Stamp,Message Contents\n";
        for (HashMap<String, String> message : history) {
            if (message.size() > 1){
                String recipient = db.get("id", message.get("recipient")).get("email");
                String sender = db.get("id", message.get("sender")).get("email");
                if (Arrays.asList(idsOfConversationsToRetrieve).contains(message.get("recipient"))) {
                    Instant time = Instant.parse(message.get("timeStamp"));
                    String timeStamp = time.atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalTime().toString();
                    text += String.join(",", message.get("messageNum"), sender, recipient, message.get("store"), timeStamp, message.get("message")) + "\n";
                }
            }
        }
        int count = 1;
        File file = new File("csv/" + id + "-historyCSV.csv");
        if (!file.createNewFile()) {
            while (!(file = new File("csv/" + id + "-historyCSV-" + count + ".csv")).createNewFile()) {
                count++;
            }
        }
        PrintWriter pw = new PrintWriter(file);
        pw.write(text.strip());
        pw.flush();
        pw.close();
    }
}
