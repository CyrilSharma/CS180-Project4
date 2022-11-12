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
    String conversationSplit = "\n#####";
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
            names.add(result.get("username"));
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
                if (!line.contains(tokenSep)) {
                    String[] senderRecipient = line.split("-");
                    sender = senderRecipient[0];
                    recipient = senderRecipient[1];
                } else {
                    HashMap<String, String> message = new HashMap<String, String>();
                    String[] lineArray = line.split(tokenSep);
                    message.put("message", lineArray[0]);
                    message.put("messageNumber", lineArray[1]);
                    message.put("recipient", lineArray[2]);
                    //checks whether the person the text file is contacting sent or recieved the message
                    if (message.get("recipient").equals(sender)) {
                        message.put("sender", recipient);
                    } else {
                        message.put("sender", sender);
                    }
                    message.put("timeStamp", lineArray[3].split("-----")[0]);
                    history.add(message);
                }
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
                boolean hasConversation = false;
                while (scan.hasNextLine() && (curLine = scan.nextLine()) != null) {
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
                while (scan.hasNextLine() && (curLine = scan.nextLine()) != null) {
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
                if (action.equals("message") || action.equals("modify")) {
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
                System.out.println(history.toString());
                String historyString = "";
                for (String str : history) {
                    historyString += str;
                }
                pw.write(historyString);
                pw.close();
                scan.close();
            }
        }
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
            String recipient = db.get("id", message.get("recipient")).get("email");
            String sender = db.get("id", message.get("sender")).get("email");
            if (Arrays.asList(idsOfConversationsToRetrieve).contains(message.get("recipient"))) {
                Instant time = Instant.parse(message.get("timeStamp"));
                String timeStamp = time.atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalTime().toString();
                text += String.join(",", message.get("messageNumber"), sender, recipient, timeStamp, message.get("message")) + "\n";
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
