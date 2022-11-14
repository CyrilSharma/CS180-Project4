import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
/**
 * Project 4 -> MessageInterface
 *
 * Specialized UI for messaging, extension off MessageManager
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class MessageInterface {

    /**
     * primary method in the MessageInterface class
     * where customers can send messages to senders and vice versa
     *
     * @param scanner, mm, db, id
     */
    public static void message(Scanner scanner, MessageManager mm, Database db, String id) {
        HashMap<String, String> h = db.get("id", id);
        HashMap<String, String> recipient;
        String email;
        String store;
        if (h.get("role").equals(Role.Customer.toString())) {
            System.out.println("Which store would you like to send a message to?");
            store = scanner.nextLine();
            email = User.getEmailFromStore(store);
            if (email == null || db.get("email", email) == null 
                || !db.get("email", email).get("role").equals(Role.Seller.toString())) {
                System.out.println("That is not a valid store");
                return;
            }
        } else if (h.get("role").equals(Role.Seller.toString())) {
            System.out.println("Who do you want to message:");
            email = scanner.nextLine();
            if (db.get("email", email) == null 
                || !db.get("email", email).get("role").equals(Role.Customer.toString())) {
                System.out.println("That is not a valid email");
                return;
            }
            /** NOT a method designator
             * select store to message
             */
            ArrayList<String> stores = User.readStoresFromFile(h.get("email"));
            System.out.println("List of stores:\n" + String.join(", ", stores));
            System.out.println("Which store would you like to send a message from:");
            store = scanner.nextLine();
            if (User.getEmailFromStore(store) == null) {
                System.out.println("That is not a valid store");
                return;
            }
        } else {
            System.out.println("You do not have permission to message that user");
            return;
        }
        recipient = db.get("email", email);
        if (recipient == null || !recipient.containsKey("blocked") 
            || recipient.get("role").equals(h.get("role")) 
            || recipient.get("blocked").contains(id) 
            || h.get("blocked").contains(recipient.get("id"))) {
            recipient = null;
            System.out.println("You do not have permission to message that user");
            return;
        }
        int resp;
        do {
            /** NOT a method designator
             * allows user to either send message through console or send contents of a text file
             */
            System.out.println("How would you like to send a message?\n1. Console\n2. Text file");
            try {
                resp = Integer.parseInt(scanner.nextLine());
                if (resp < 1 || resp > 2) {
                    System.out.println("That is not a valid input. Please try again");
                    resp = -1;
                }
            } catch (Exception e) {
                System.out.println("That is not a valid input. Please try again");
                resp = -1;
            }
        } while (resp == -1);
        String[] message = null;
        if (resp == 1) {
            System.out.println("Type your message and press enter:");
            message = new String[]{scanner.nextLine()};
        } else {
            do {
                System.out.println("Type the path to your text file");
                try {
                    message = mm.readTextFromFile(scanner.nextLine());
                } catch (FileNotFoundException e) {
                    System.out.println("That file does not exist. Please try again");
                    message = null;
                }
            } while (message == null);
        }
        for (String messages : message) {
            mm.messageUser(h.get("id"), recipient.get("id"), messages, store);
        }
        System.out.println("Message sent");
    }


    /**
     * Allows user to view a specific conversation with a prompted user
     *
     * @param scanner, id, db, mm
     */
    public static ArrayList<HashMap<String, String>> viewMessageHistory(Scanner scanner, String id, 
        Database db, MessageManager mm, boolean on, Filter filter, boolean showNumbers) {
        ArrayList<String> conversationPartners = MessageInterface.listConversations(id, db, mm);
        if (conversationPartners.isEmpty()) {
            return null;
        }
        System.out.println("Enter the number or email you wish to see message history for");
        String otherUserEmail;
        if (scanner.hasNextInt()) {
            int v = Integer.parseInt(scanner.nextLine());
            while (v > conversationPartners.size() || v < 1) {
                System.out.println("That is not a valid input");
                System.out.println("Enter the number or email you wish to see message history for");
                v = Integer.parseInt(scanner.nextLine());
            }
            otherUserEmail = conversationPartners.get(v - 1);
        } else {
            otherUserEmail = scanner.nextLine();
        }
        String userEmail = db.get("id", id).get("email");
        HashMap<String, String> user = db.get("email", userEmail);
        HashMap<String, String> otherUser = db.get("email", otherUserEmail);
        if (otherUser == null) {
            System.out.println("The user you were messaging doesn't seem to exist");
            return null;
        }
        ArrayList<HashMap<String, String>> history;
        try {
            history = mm.getPersonalHistory(user.get("id"));
        } catch (IOException e) {
            System.out.println("You don't seem to have any conversation history");
            return null;
        }
        String conversations = "";
        int startLocation = -1;
        for (int i = 0; i < history.size(); i++) {
            HashMap<String, String> message = history.get(i);
            if (message.size() == 1 && message.containsKey("recipient") 
                && message.get("recipient").equals(otherUser.get("id"))) {
                startLocation = i;
                break;
            }
        }
        int x = startLocation + 1;
        ArrayList<HashMap<String, String>> listOfMessages = new ArrayList<HashMap<String, String>>();
        int i = 1;
        while (history.get(x).size() > 1) {
            HashMap<String, String> message = history.get(x);
            listOfMessages.add(message);
            String content = message.get("message");
            String sender = db.get("id", message.get("sender")).get("email");
            String recipient = db.get("id", message.get("recipient")).get("email");
            if (db.get("id", message.get("sender")).get("role").equals(Role.Customer.toString())) {
                recipient += " (" + message.get("store") + ")";
            } else {
                sender += " (" + message.get("store") + ")";
            }
            Instant time = Instant.parse(message.get("timeStamp"));
            String timeString = time.atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalTime().toString();
            if (showNumbers) {
                conversations += String.format("%d. %s->%s at %s: %s\n", i, sender, recipient, timeString, content);
            } else {
                conversations += String.format("%s->%s at %s: %s\n", sender, recipient, timeString, content);
            }
            x++;
            i++;
        }
        System.out.println("Message History (oldest first):");
        if (on) {
            System.out.println(filter.filter(conversations.strip()));
        } else {
            System.out.println(conversations.strip());
        }
        return listOfMessages;
    }

    /**
     * Lists out all of a user's conversations
     * @param db, id, mm
     */
    public static ArrayList<String> listConversations(String id, Database db, MessageManager mm) {
        ArrayList<String> ids = new ArrayList<String>();
        try {
            System.out.println("Your conversations:");
            ArrayList<HashMap<String, String>> conversations = mm.getPersonalHistory(id);
            int x = 1;
            for (int i = 0; i < conversations.size(); i++) {
                HashMap<String, String> convo = conversations.get(i);
                if (convo.size() == 1 && !convo.containsKey("messageBreak")) {
                    String otherID = db.get("id", convo.get("recipient")).get("email");
                    ids.add(otherID);
                    String email =  otherID;
                    System.out.println(x + ". " + email);
                    x++;
                }
            }
            if (x == 1) {
                System.out.println("You do not appear to have any conversations");
            }
        } catch (IOException e) {
            System.out.println("You don't have a conversation history at the moment");
        }
        return ids;
    }

    /**
     * Allows user to export specific conversations with a prompted user
     *
     * @param scanner, id, db, mm
     */
    public static void exportConversations(Scanner scanner, String id, Database db, MessageManager mm) {
        ArrayList<String> ids = listConversations(id, db, mm);
        if (ids.isEmpty()) {
            return;
        }
        System.out.println("For which users would you like to export conversations (EMAIL, EMAIL):");
        String[] input = scanner.nextLine().split(", ");
        for (int i = 0; i < input.length; i++) {
            try {
                input[i] = ids.get(Integer.parseInt(input[i]) - 1);
            } catch (Exception e) {
                e.getMessage();
            }
            try {
                input[i] = db.get("email", input[i]).get("id");
                if (input[i] == null) {
                    System.out.println("It looks like one of those emails is invalid");
                    return;
                }
            } catch (NullPointerException e) {
                System.out.println("It looks like one of those emails is invalid");
                return;
            }
        }
        try {
            mm.messagesToCSV(id, input);
            System.out.println("CSV generated");
        } catch (IOException e) {
            System.out.println("Unable to export those conversations to CSV");
        }
    }

    /**
     * Feature for user to see new messages sent since they were last online
     */
    public static void missedMessages(Scanner scanner, String id, Database db, MessageManager mm) {
        Instant lastOnline = Instant.parse(db.get("id", id).get("lastOnline"));
        try {
            ArrayList<HashMap<String, String>> history = mm.getPersonalHistory(id);
            ArrayList<HashMap<String, String>> missedMessages = new ArrayList<HashMap<String, String>>();
            for (HashMap<String, String> message : history) {
                if (message.size() > 1) {
                    if (Instant.parse(message.get("timeStamp")).isAfter(lastOnline)) {
                        missedMessages.add(message);
                    }
                }
            }
            sort(missedMessages);
            String conversations = "Missed Messages:\n";
            for (HashMap<String, String> message : missedMessages) {
                String content = message.get("message");
                String sender = db.get("id", message.get("sender")).get("email");
                String recipient = db.get("id", message.get("recipient")).get("email");
                if (db.get("id", message.get("sender")).get("role").equals(Role.Customer.toString())) {
                    recipient += " (" + message.get("store") + ")";
                } else {
                    sender += " (" + message.get("store") + ")";
                }
                Instant time = Instant.parse(message.get("timeStamp"));
                String timeString = time.atZone(Calendar.getInstance().getTimeZone().toZoneId())
                    .toLocalTime().toString();
                conversations += String.format("%s->%s at %s: %s\n", sender, recipient, timeString, content);
            }
            if (missedMessages.isEmpty()) {
                conversations = "You don't have any missed messages";
            }
            System.out.println("\n" + conversations);
        } catch (IOException e) {
            System.out.println("There was a problem accessing your history");
        }
    }

    /**
     * Sort conversations based on timestamp of last message (newest to oldest)
     */
    private static void sort(ArrayList<HashMap<String, String>> messages) {
        boolean sorted = false;
        HashMap<String, String> previousMessage = null;
        while (sorted == false) {
            sorted = true;
            for (int i = 0; i < messages.size(); i++) {
                HashMap<String, String> message = messages.get(i);
                if (previousMessage == null) {
                    previousMessage = message;
                } else {
                    if (Instant.parse(previousMessage.get("timeStamp"))
                        .isAfter(Instant.parse(message.get("timeStamp")))) {
                        sorted = false;
                        messages.set(i - 1, message);
                        messages.set(i, previousMessage);
                    }
                    previousMessage = messages.get(i);
                }
            }
        }
    }
}
