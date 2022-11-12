import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private Random random;
    private Database db;
    private String historyDir;

    public MessageManager(String dbPath, String historyPath) {
        db = new Database(dbPath);
        historyDir = historyPath;
        random = new Random();
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
                    recipient = line;
                    message.put("recipient", recipient);
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
                    message.put("timeStamp", lineArray[3].split("-----")[0]);
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

    public void messageUser(String senderID, String recipientID, String message) {
        try {
            generalMessage(senderID, recipientID, message, "message", "");
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
                historyLoc.put("recipient", recipientID);


                int startLocation = history.indexOf(historyLoc);
                historyLoc = new HashMap<String, String>();
                historyLoc.put("recipient", senderID);


                HashMap<String, String> newEntry = new HashMap<String, String>();
                newEntry.put("message", message);
                newEntry.put("recipient", recipientID);
                newEntry.put("messageNum", messageID);
                newEntry.put("timeStamp", Instant.now().toString());

                int i = startLocation;
                if (startLocation == -1) {
                    HashMap<String, String> temp = new HashMap<String, String>();
                    temp.put("recipient", recipientID);
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

    private String formatMessages(ArrayList<HashMap<String, String>> messages) {
        String messageString = "";
        for (HashMap<String, String> things : messages) {
            if (things.size() > 1) {
                messageString += String.join(tokenSep, things.get("message"), things.get("recipient"), things.get("messageNum"), things.get("timeStamp") + messageSplit + "\n");
            } else if (things.containsKey("recipient")) {
                messageString += things.get("recipient") + "\n";
            } else {
                messageString += things.get("messageBreak") + "\n";
            }
        }
        return messageString.strip();
    }

    public String readTextFromFile(String path) throws FileNotFoundException {
        String text = "";
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File(path)))) {
            ArrayList<String> list = new ArrayList<String>();
            String line;
            while ((line = bfr.readLine()) != null) {
                list.add(line);
            }
            text = String.join("\n", list);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public void messagesToCSV(String id, String[] idsOfConversationsToRetrieve) throws IOException {
        ArrayList<HashMap<String, String>> history = getPersonalHistory(id);
        String text = "Message ID, Sender,Recipient,Time Stamp,Message Contents\n";
        for (HashMap<String, String> message : history) {
            if (message.size() > 1){
                String recipient = db.get("id", message.get("recipient")).get("email");
                String sender = db.get("id", message.get("sender")).get("email");
                if (Arrays.asList(idsOfConversationsToRetrieve).contains(message.get("recipient"))) {
                    Instant time = Instant.parse(message.get("timeStamp"));
                    String timeStamp = time.atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalTime().toString();
                    text += String.join(",", message.get("messageNum"), sender, recipient, timeStamp, message.get("message")) + "\n";
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
