import java.util.*;
import java.io.*;
import java.time.Instant;

public class MessageInterfaceClient {
    private Client client;
    public MessageInterfaceClient() {
        client = new Client();
    }
    public String message(String message, String id, String recipientId, String store) {
        String object = "messageManager";
        String function = "message";
        String[] args = {id, recipientId, message, store};
        Object o = client.query(new Query(object, function, args));
        if (o == null) {
            // handled by caller method.
            return "INVALID";
        }
        return "SUCCESS";
    }

    public String editMessage(String id, String recipientId, String newMessage, String messageID) {
        String object = "messageManager";
        String function = "editMessage";
        String[] args = {id, recipientId, newMessage, messageID};
        Object o = client.query(new Query(object, function, args));
        if (o == null) {
            // handled by caller method.
            return "INVALID";
        }
        return "SUCCESS";
    }

    public String deleteMessage(String id, String recipientId, String messageID) {
        String object = "messageManager";
        String function = "deleteMessage";
        String[] args = {id, recipientId, messageID};
        Object o = client.query(new Query(object, function, args));
        if (o == null) {
            // handled by caller method.
            return "INVALID";
        }
        return "SUCCESS";
    }

    public ArrayList<HashMap<String, String>> getPersonalHistory(String id) {
        String object = "messageManager";
        String function = "getPersonalHistory";
        String[] args = {id};
        Object o = client.query(new Query(object, function, args));
        if (o == null) {
            return null;
        }
        return (ArrayList<HashMap<String, String>>) o;
    }
    
    public ArrayList<HashMap<String, String>> missedMessages(Scanner scanner, String id, Client client) {
        Instant lastOnline = Instant.parse(client.get("id", id).get("lastOnline"));
        try {
            ArrayList<HashMap<String, String>> history = getPersonalHistory(id);
            ArrayList<HashMap<String, String>> missedMessages = new ArrayList<HashMap<String, String>>();
            for (HashMap<String, String> message : history) {
                if (message.size() > 1) {
                    if (Instant.parse(message.get("timeStamp")).isAfter(lastOnline)) {
                        missedMessages.add(message);
                    }
                }
            }
            sort(missedMessages);
            return missedMessages;
        } catch (Exception e) {
            System.out.println("There was a problem accessing your history");
            return null;
        }
    }

    public ArrayList<String> listConversations(String id) {
        ArrayList<String> ids = new ArrayList<String>();
        System.out.println("Your conversations:");
        ArrayList<HashMap<String, String>> conversations = getPersonalHistory(id);
        int x = 1;
        for (int i = 0; i < conversations.size(); i++) {
            HashMap<String, String> convo = conversations.get(i);
            if (convo.size() == 1 && !convo.containsKey("messageBreak")) {
                String otherID = client.get("id", convo.get("recipient")).get("email");
                ids.add(otherID);
                x++;
            }
        }
        if (x == 1) {
            System.out.println("You do not appear to have any conversations");
        }
        return ids;
    }

    public void exportConversations(String id) {
        String object = "messageManager";
        String function = "exportConversations";
        String[] args = new String[]{id};
        client.query(new Query(object, function, args));
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
