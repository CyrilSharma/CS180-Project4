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

    public MessageManager(String path) {
        db = new Database(path);
        random = new Random();
    }

    //Returns the names of all the customers a user can talk to
    public ArrayList<String> getNames(String type) throws InvalidUserException {
        ArrayList<HashMap<String, String>> results = db.getSelection("role", type);
        ArrayList<String> names = new ArrayList<String>();
        for (HashMap<String, String> result: results) {
            names.add(result.get("email"));
        }
        return names;
    }

    public ArrayList<HashMap<String, String>> getPersonalHistory(String id) throws IOException {
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File("history/" + id + "-messageHistory.txt")))) {
            ArrayList<HashMap<String, String>> history = new ArrayList<HashMap<String, String>>();
            String line;
            String recipient = "";
            String sender = "";
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
        File[] files = {new File("history/" + senderID + "-messageHistory.txt"),
            new File("history/" + recipientID + "-messageHistory.txt")};
        files[0].createNewFile();
        files[1].createNewFile();
        ArrayList<HashMap<String, String>> senderHistory = getPersonalHistory(senderID);
        ArrayList<HashMap<String, String>> recipientHistory = getPersonalHistory(recipientID);
        if (action.equals("message")) {
            if (messageID == null || messageID.equals("")) {
                messageID = "";
                //TODO: Add do-while loop to this
                    for (int i = 0; i < 14; i++) {
                        int num = random.nextInt(50);
                        messageID += (char)('0' + num);
                    }
            }
            HashMap<String, String> historyLoc = new HashMap<String, String>();
            historyLoc.put("recipient", recipientID);
            int startLocation = senderHistory.indexOf(historyLoc);
            historyLoc = new HashMap<String, String>();
            historyLoc.put("recipient", senderID);
            int startLocation2 = recipientHistory.indexOf(historyLoc);
            HashMap<String, String> newEntry = new HashMap<String, String>();
            newEntry.put("message", message);
            newEntry.put("recipient", recipientID);
            newEntry.put("messageNum", messageID);
            newEntry.put("timeStamp", Instant.now().toString());
            int i = startLocation;
            int i2 = startLocation2;
            if (startLocation == -1 || startLocation2 == -1) {
                if (startLocation == -1) {
                    HashMap<String, String> temp = new HashMap<String, String>();
                    temp.put("recipient", recipientID);
                    senderHistory.add(temp);
                    HashMap<String, String> temp2 = new HashMap<String, String>();
                    temp2.put("messageBreak", conversationSplit);
                    senderHistory.add(temp2);
                    i = senderHistory.size() - 1;
                }
                if (startLocation2 == -1) {
                    HashMap<String, String> temp = new HashMap<String, String>();
                    temp.put("recipient", senderID);
                    recipientHistory.add(temp);
                    HashMap<String, String> temp2 = new HashMap<String, String>();
                    temp2.put("messageBreak", conversationSplit);
                    recipientHistory.add(temp2);
                    i2 = recipientHistory.size() - 1;
                }
            } else {
                HashMap<String, String> line = senderHistory.get(i);
                while (!line.containsKey("messageBreak")) {
                    i++;
                    line = senderHistory.get(i);
                }
                line = recipientHistory.get(i2);
                while (!line.containsKey("messageBreak")) {
                    i2++;
                    line = recipientHistory.get(i2);
                }
            }
            senderHistory.add(i, newEntry);
            recipientHistory.add(i2, newEntry);
        } else if (action.equals("edit")) {
            for (HashMap<String, String> sendHist : senderHistory) {
                if (sendHist.containsKey("messageNum") && sendHist.get("messageNum").equals(messageID)) {
                    sendHist.put("message", message);
                    break;
                }
            }
            for (HashMap<String, String> recipHist : recipientHistory) {
                if (recipHist.containsKey("messageNum") && recipHist.get("messageNum").equals(messageID)) {
                    recipHist.put("message", message);
                    break;
                }
            }
        } else if (action.equals("delete")){
            for (HashMap<String, String> sendHist : senderHistory) {
                if (sendHist.containsKey("messageNum") && sendHist.get("messageNum").equals(messageID)) {
                    senderHistory.remove(sendHist);
                    break;
                }
            }
        }
        PrintWriter pw = new PrintWriter(files[0]);
        pw.write(formatMessages(senderHistory));
        pw.flush();
        pw.close();
        pw = new PrintWriter(files[1]);
        pw.write(formatMessages(recipientHistory));
        pw.flush();
        pw.close();
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
        Path path = Path.of("csv");
        List<Path> filesInDir = Files.list(path).toList();
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
