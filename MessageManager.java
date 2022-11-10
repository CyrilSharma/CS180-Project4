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
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File(id + "-messageHistory.txt")))) {
            ArrayList<HashMap<String, String>> history = new ArrayList<HashMap<String, String>>();
            String line;
            String sender = id;
            String recipient = "";
            while ((line = bfr.readLine()) != null) {
                if (!line.contains("#####")) {
                    recipient = line;
                } else {
                    HashMap<String, String> message = new HashMap<String, String>();
                    message.put("message", line.split("\\|\\|\\|\\|\\|")[0]);
                    line = line.split("\\|\\|\\|\\|\\|")[1];
                    message.put("messageNumber", line.split("#####")[0]);
                    line = line.split("#####")[1];
                    message.put("timeStamp", line.split("-----")[0]);
                    message.put("recipient", recipient);
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
        String text = "Message Number,Recipient,Time Stamp,Message Contents\n";
        for (HashMap<String, String> message : history) {
            String recipient = db.get("id", message.get("recipient")).get("email");
            if (Arrays.asList(idsOfConversationsToRetrieve).contains(message.get("recipient"))) {
                Instant time = Instant.parse(message.get("timeStamp"));
                String timeStamp = time.atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalTime().toString();
                text += String.join(",", message.get("messageNumber"), recipient, timeStamp, message.get("message")) + "\n";
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
