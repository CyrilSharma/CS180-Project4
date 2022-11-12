import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MessageInterface {
    
    public static void message(Scanner scanner, MessageManager mm, Database db, String id) {
        HashMap<String, String> h = db.get("id", id);
        User me = new User(h.get("email"), h.get("password"), h.get("role"));
        try {
            if (h.get("role").equals(Role.Customer.toString())) {
                me.viewStores();
            } else {
                me.viewCustomers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> recipient;
        System.out.println("Who would you like to send a message to?");
        recipient = db.get("email", scanner.nextLine());
        if (!recipient.containsKey("blocked") || recipient.get("role").equals(h.get("role")) || recipient.get("blocked").contains(id)) {
            recipient = null;
            System.out.println("You do not have permission to message that user");
            return;
        }
        int resp;
        do {
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
        String message = null;
        if (resp == 1) {
            System.out.println("Type your message and press enter:");
            message = scanner.nextLine();
        }
        else {
            do {
                System.out.println("Type the path to your text file");
                try {
                    message = mm.readTextFromFile(scanner.nextLine());
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    System.out.println("That file does not exist. Please try again");
                    message = null;
                }
            } while (message == null);
        }
        if (h.get("role").equals(Role.Customer.toString())) {
            try {
                me.selectStore(recipient.get("id"), message);
            } catch (InvalidUserException | IOException e) {
                // TODO Auto-generated catch block
                System.out.println("You are not able to message that user");
            }
        } else {
            try {
                me.selectCustomer(recipient.get("id"), message);
            } catch (InvalidUserException | IOException e) {
                // TODO Auto-generated catch block
                System.out.println("You are not allowed to message that user");
            }
        }
    }

    public static void viewMessageHistory(String userEmail, String otherUserEmail, Database db, MessageManager mm) {
        HashMap<String, String> user = db.get("email", userEmail);
        HashMap<String, String> otherUser = db.get("email", otherUserEmail);
        if (otherUser == null) {
            System.out.println("The user you were messaging doesn't seem to exist");
            return;
        }
        ArrayList<HashMap<String, String>> history;
        try {
            history = mm.getPersonalHistory(user.get("id"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("You don't seem to have any conversation history");
            return;
        }
        String conversations = "";
        int startLocation = -1;
        for (int i = 0; i < history.size(); i++) {
            HashMap<String, String> message = history.get(i);
            if (message.size() == 1 && message.containsKey("recipient") && message.get("recipient").equals(otherUser.get("id"))) {
                startLocation = i;
                break;
            }
        }
        int x = startLocation;
        while (history.get(x).size() > 1) {
            x++;
            HashMap<String, String> message = history.get(x);
            String content = message.get("message");
            String recipient;
            String sender;
            if (message.get("recipient").equals(otherUser.get("id"))) {
                sender = userEmail;
                recipient = otherUserEmail;
            } else {
                sender = otherUserEmail;
                recipient = userEmail;
            }
            String timeString = message.get("timeStamp");
            conversations = String.format("%s->%s at %s: %s\n", sender, recipient, timeString, content) + conversations;
        }
        System.out.println(conversations.strip());
    }
}
