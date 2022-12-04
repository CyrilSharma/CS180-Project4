import java.util.*;
import java.io.*;
import java.time.Instant;

public class MessageInterfaceClient {
    private Translator translator;
    public MessageInterfaceClient() {
        translator = new Translator();
    }
    public String message(String message, String id, String recipientId, String store) throws Exception {
        String object = "MessageManager";
        String function = "messageUser";
        String[] args = {id, recipientId, message, store};
        Object o = translator.query(new Query(object, function, args));
        if (o == null) {
            // handled by caller method.
            return "INVALID";
        }
        return "SUCCESS";
    }

    public String editMessage(String id, String recipientId, String newMessage, String messageID) throws Exception {
        String object = "MessageManager";
        String function = "editMessage";
        String[] args = {id, recipientId, newMessage, messageID};
        Object o = translator.query(new Query(object, function, args));
        if (o == null) {
            // handled by caller method.
            return "INVALID";
        }
        return "SUCCESS";
    }

    public String deleteMessage(String id, String recipientId, String messageID) throws Exception {
        String object = "MessageManager";
        String function = "deleteMessage";
        String[] args = {id, recipientId, messageID};
        Object o = translator.query(new Query(object, function, args));
        if (o == null) {
            // handled by caller method.
            return "INVALID";
        }
        return "SUCCESS";
    }

    public ArrayList<HashMap<String, String>> getPersonalHistory(String id) throws Exception {
        String object = "MessageManager";
        String function = "getPersonalHistory";
        String[] args = {id};
        Object o = translator.query(new Query(object, function, args));
        if (o == null) {
            return null;
        }
        return (ArrayList<HashMap<String, String>>) o;
    }
    
    public ArrayList<HashMap<String, String>> missedMessages(Scanner scanner, String id, Translator client) throws Exception {
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
            throw new Exception("There was a problem accessing your history");
        }
    }

    public void exportConversations(String id, String[] otherIDs) throws Exception {
        String object = "MessageManager";
        String function = "messagesToCSV";
        //TODO: change this to objects, I guess
        Object[] args = new Object[]{id, otherIDs};
        translator.query(new Query(object, function, args));
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

    public String getID() throws Exception {
        return (String) translator.query(new Query("User", "getID"));
    }

    public ArrayList<Message> getConversation(String id, String store) throws Exception {
        String object = "MessageManager";
        String function = "getConversation";
        String[] args = new String[]{getID(), id, store};
        return (ArrayList<Message>) translator.query(new Query(object, function, args));
    }

    public Translator getTranslator() {
        return translator;
    }

    public String[] messagesToArray(ArrayList<Message> conversationHistory) throws Exception {
        String[] messages = new String[conversationHistory.size()];
        for (int i = 0; i < conversationHistory.size(); i++) {
            messages[i] = conversationHistory.get(i).getMessage();
        }
        return messages;
    }
}
