import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
    
    public void messageUser(String sender, String recipient, String message) throws IOException {
        //TODO: Implement messageUser
        int messageCount;
        String messageSplit = "-----";
        LocalTime time = LocalTime.now();
        DateTimeFormatter myFormatTime = DateTimeFormatter.ofPattern(("MM-dd-yyyy HH:mm"));
        //timestamp = myFormatTime variable
        boolean previousLine = false;
        boolean convExists = false;
        String senderID = String.valueOf(db.getSelection("id", sender));
        String recipientID = String.valueOf(db.getSelection("id", recipient));


        File f = new File(senderID + "-messageHistory.txt"); //format: 1231-messageHistory.txt
        File f2 = new File(recipientID + "-messageHistory.txt"); //format: 1231-messageHistory.txt
        ArrayList<String> history = new ArrayList<String>();
        if (f.createNewFile() || f.length() == 0) {
            //for sender
            //messageHistory does not exist
            FileOutputStream fos = new FileOutputStream(f, false);
            PrintWriter pw1 = new PrintWriter(fos);
            messageCount = 1;
            pw1.println(db.getSelection("role", "Customer") + "-" + db.getSelection("role", "Seller") + "-" /* + get stores*/);
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
            if (f2.createNewFile() || f2.length() == 0) {
                //for recipient
                //messageHistory does not exist
                FileOutputStream fos = new FileOutputStream(f2, false);
                PrintWriter pw1 = new PrintWriter(fos);
                messageCount = 1;
                pw1.println(db.getSelection("role", "Customer") + "-" + db.getSelection("role", "Seller") + "-" /* + get stores*/);
                pw1.println(messageCount + "<" + myFormatTime + ">" + sender + ": " + message);
                pw1.println(messageSplit);
                fos.close();
                pw1.close();
            } else {
                BufferedReader br2 = new BufferedReader(new FileReader(f2));
                PrintWriter pw2 = new PrintWriter(new FileWriter(f2), false);
                String line2 = br.readLine();
                while (line2 != null) {
                    history.add(line2);
                    br2.readLine();
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
                pw2.close();
                br2.close();
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
