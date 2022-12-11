import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.time.Instant;
/**
 * Project 5 -> MessageManager
 *
 * This class handles message sending for a particular user. It specifically adds create, edit, and remove message
 * functionality as well as the means to export conversations into a .CSV file.
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class MessageManager {
    String tokenSep = "|||||";
    String messageSplit = "-----";
    String conversationSplit = "#####";
    private Random random;
    private Database db;
    private String historyDir;

    /**
     * Initializes instance fields
     *
     * @param db, historyPath
     */
    public MessageManager(Database db) {
        this.db = db;
        historyDir = PathManager.storeDir + "history/";
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

    public String getHistoryLocation(String id) {
        return historyDir + id + "-messageHistory.txt";
    }

    /**
     * Returns the read status of all the users conversations.
     */
    public synchronized HashMap<String, HashMap<String, Boolean>> getReadStatus(String email) throws IOException {
        File f = new File(historyDir + email + "-convosRead.txt");
        f.createNewFile();
        try (BufferedReader bfr = new BufferedReader(new FileReader(f))) {
            HashMap<String, Boolean> read = new HashMap<String, Boolean>();
            HashMap<String, HashMap<String, Boolean>> read2 = new HashMap<>();
            String line;
            while ((line = bfr.readLine()) != null) {
                String[] tokens = line.split(Pattern.quote(tokenSep));
                if (line.equals("")) {
                    continue;
                }
                read.put(tokens[1], tokens[2].equals("true"));
                read2.put(tokens[0], read);
            }
            return read2;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Updates the read status of a conversation.
     */
    public synchronized void updateReadStatus(String sender, String recepient, String store) {
        HashMap<String, Boolean> read;
        HashMap<String, HashMap<String, Boolean>> read2;
        try {
            read2 = getReadStatus(sender);
            read = read2.get(recepient);
            if (read == null) {
                read = new HashMap<>();
            }
            read.put(store, true);
            read2.put(recepient, read);
            saveReadStatus(sender, read2);

            read2 = getReadStatus(recepient);
            read = read2.get(sender);
            if (read == null) {
                read = new HashMap<>();
            }
            read.put(store, false);
            read2.put(sender, read);
            saveReadStatus(recepient, read2);
        } catch (Exception e) {}
    }

    public synchronized void updateReadStatusSelf(String self, String other, String store) {
        HashMap<String, Boolean> read;
        HashMap<String, HashMap<String, Boolean>> read2;
        try {
            read2 = getReadStatus(self);
            read = read2.get(other);
            if (read == null) {
                read = new HashMap<>();
            }
            read.put(store, true);
            read2.put(other, read);
            saveReadStatus(self, read2);
        } catch (Exception e) {
        }
    }

    /**
     * Save the read status of a users conversations.
     */
    public synchronized void saveReadStatus(String sender, HashMap<String, HashMap<String, Boolean>> entries) throws IOException {
        File f = new File(historyDir + sender + "-convosRead.txt");
        HashSet<String> entered = new HashSet<String>();
        f.createNewFile();
        try (PrintWriter pw = new PrintWriter(f)) {
            for (String user: entries.keySet()) {
                String line = "%s%s%s%s%s\n";
                for (Map.Entry<String,Boolean> entry : entries.get(user).entrySet()) {
                    if (entered.contains(user + entry.getKey())) {
                        continue;
                    } else {
                        entered.add(user + entry.getKey());
                    }
                    line = String.format(line, user, tokenSep, entry.getKey(),
                            tokenSep, entry.getValue());
                    pw.write(line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * Returns the personal message history of the user
     */public synchronized ArrayList<HashMap<String, String>> getPersonalHistory(String id) throws IOException {
        File f = new File(historyDir + id + "-messageHistory.txt");
        f.createNewFile();
        try (BufferedReader bfr = new BufferedReader(new FileReader(f))) {
            ArrayList<HashMap<String, String>> history = new ArrayList<HashMap<String, String>>();
            String line;
            String recipient = "";
            String messageString = "";
            while ((line = bfr.readLine()) != null) {
                HashMap<String, String> message = new HashMap<String, String>();
                if (!line.contains(tokenSep) && !line.contains(conversationSplit) && (history.isEmpty() || history.get(history.size() - 1).containsKey("messageBreak"))) {
                    message.put("recipient", line);
                    recipient = line;
                    history.add(message);
                } else if (line.contains(conversationSplit)) {
                    message.put("messageBreak", line);
                    history.add(message);
                } else if (!line.contains("|||||")) {
                    messageString += line + "\n";
                } else {
                    String[] lineArray = line.split("\\|\\|\\|\\|\\|");
                    messageString += lineArray[0];
                    message.put("message", messageString);
                    messageString = "";
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
                    history.add(message);
                }
            }
            return history;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            return null;
        }
    }

    public ArrayList<Message> getConversation(String id, String otherID, String store) throws IOException {
        ArrayList<HashMap<String, String>> personalHistory = getPersonalHistory(id);
        ArrayList<Message> messages = new ArrayList<>();
        for (HashMap<String, String> message : personalHistory) {
            if (message.size() > 1 && (message.get("sender").equals(otherID) || message.get("recipient").equals(otherID)) && message.get("store").equals(store)) {
                String messageContent = message.get("message");
                String messageNum = message.get("messageNum");
                String messageSender = message.get("sender");
                String messageRecipient = message.get("recipient");
                Instant messageTimeStamp = Instant.parse(message.get("timeStamp"));
                String messageStore = message.get("store");
                messages.add(new Message(messageContent, messageNum, messageSender, messageRecipient, messageTimeStamp, messageStore));
            }
        }
        return messages;
    }

    public ArrayList<String> listConversations(String id) throws IOException {
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<HashMap<String, String>> conversations = getPersonalHistory(id);
        for (int i = 0; i < conversations.size(); i++) {
            HashMap<String, String> convo = conversations.get(i);
            if (convo.size() == 1 && !convo.containsKey("messageBreak")) {
                String otherID = db.get("id", convo.get("recipient")).get("email");
                ids.add(otherID);
            }
        }
        return ids;
    }

    /**
     * Allows the user to message other users (Customers to Sellers and vv,
     * NOT Customers to Customers or Sellers to Sellers
     */
    public void messageUser(String senderID, String recipientID, String message, String store) throws Exception {
        try {
            generalMessage(senderID, recipientID, message, "message", "", store);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Allows the user to edit a message in an existing conversation
     * @throws Exception
     */
    public void editMessage(String senderID, String recipientID, String message, String messageId) throws Exception {
        try {
            generalMessage(senderID, recipientID, message, "edit", messageId, "");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Allows the user to delete a message from THEIR personal message history
     * @throws Exception
     */
    public void deleteMessage(String senderID, String recipientID, String messageId) throws Exception {
        try {
            generalMessage(senderID, recipientID, "", "delete", messageId, "");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Allows the user to delete a message from THEIR personal message history
     */
    public void generalMessage(String senderID, String recipientID, String message, String action,
                               String messageID, String store) throws Exception {
        String[] ids = {senderID, recipientID};
        HashMap<String, String> senderHashMap = db.get("id", senderID);
        HashMap<String, String> recipientHashMap = db.get("id", recipientID);
        if (senderHashMap.values().toString().contains(recipientID) || recipientHashMap.values().contains(senderID)) {
            throw new Exception("You don't have permission to message that user");
        }
        for (String id : ids) {
            ArrayList<HashMap<String, String>> history = getPersonalHistory(id);
            if (action.equals("message")) {
                if (messageID == null || messageID.equals("")) {
                    do {
                        messageID = "";
                        for (int i = 0; i < 14; i++) {
                            int num = random.nextInt(50);
                            messageID += (char)('0' + num);
                        }
                    } while (db.get("id", messageID) != null);
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
            } else if (action.equals("delete")) {
                if (id.equals(senderID)) {
                    for (HashMap<String, String> hist : history) {
                        if (hist.containsKey("messageNum") && hist.get("messageNum").equals(messageID)) {
                            history.remove(hist);
                            break;
                        }
                    }
                }
            }

            // update read notifs on message.
            String sender = db.get("id", senderID).get("email");
            String recepient = db.get("id", recipientID).get("email");
            HashMap<String, HashMap<String,Boolean>> read = getReadStatus(sender);
            HashMap<String,Boolean> miniMap;
            if (read.containsKey(recepient)) {
                miniMap = read.get(recepient);
            } else {
                miniMap = new HashMap<>();
            }
            miniMap.put(store, true);
            read.put(recepient, miniMap);
            saveReadStatus(sender, read);
            // mark recepient conversation unread.
            read = getReadStatus(recepient);
            if (read.containsKey(sender)) {
                miniMap = read.get(sender);
            } else {
                miniMap = new HashMap<>();
            }
            miniMap.put(store, false);
            read.put(sender, miniMap);
            saveReadStatus(recepient, read);

            File f = new File(historyDir + id + "-messageHistory.txt");
            PrintWriter pw = new PrintWriter(f);
            pw.write(formatMessages(history));
            pw.flush();
            pw.close();
        }
    }

    /**
     * Allows the user to delete a message from THEIR personal message history
     */
    private String formatMessages(ArrayList<HashMap<String, String>> messages) {
        String messageString = "";
        for (HashMap<String, String> things : messages) {
            if (things.size() > 1) {
                messageString += String.join(tokenSep, things.get("message"), things.get("recipient"),
                        things.get("messageNum"), things.get("timeStamp"),
                        things.get("store") + messageSplit + "\n");
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
     * @throws IOException
     */
    public String[] readTextFromFile(String path) throws IOException {
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
            throw new IOException("An error has occurred sending your message");
        }
        return text;
    }

    /**
     * Converts personal message history text file into a CSV file that can be exported
     */
    public String messagesToCSV(String id, String[] idsOfConversationsToRetrieve) throws IOException {
        ArrayList<HashMap<String, String>> history = getPersonalHistory(id);
        ArrayList<String> ids = new ArrayList<>(Arrays.asList(idsOfConversationsToRetrieve));
        String text = "Message Number,Sender,Recipient,Store,Time Stamp,Message Contents\n";
        int i = 0;
        for (HashMap<String, String> message : history) {
            if (message.size() > 1) {
                String recipient = db.get("id", message.get("recipient")).get("email");
                String sender = db.get("id", message.get("sender")).get("email");
                if (ids.contains(message.get("recipient")) || ids.contains(message.get("sender"))) {
                    i++;
                    Instant time = Instant.parse(message.get("timeStamp"));
                    String timeStamp = time.atZone(Calendar.getInstance().getTimeZone().toZoneId())
                            .toLocalDateTime().toString();
                    text += String.join(",", String.valueOf(i),
                            sender, recipient, message.get("store"), timeStamp,
                            "\"" + (message.get("message")).replace("\"", "\"\"")) + "\"\n";
                }
            }
        }
        return text;
    }

    public void removeHistory(String email) {
        File file = new File(historyDir + db.get("email", email).get("id") + "-messageHistory.txt");
        file.delete();
    }
}