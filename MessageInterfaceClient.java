public class MessageInterfaceClient {
    private Translator translator;
    public MessageInterfaceClient() {
        translator = new Translator();
    }
    public String message(String message, String id) {
        String object = "messageManager";
        String function = "message";
        String[] args = {message, id};
        String[] status = translator.query(new Query(object, function, args));
        if (status[0] == "INVALID") {
            // handled by caller method.
            return "INVALID";
        }
        return "SUCCESS";
    }

    public String editMessage(String id, String messageID) {
        String object = "messageManager";
        String function = "editMessage";
        String[] args = {id, messageID};
        String[] status = translator.query(new Query(object, function, args));
        if (status[0] == "INVALID") {
            // handled by caller method.
            return "INVALID";
        }
        return "SUCCESS";
    }

    public String deleteMessage(String id, String messageID) {
        String object = "messageManager";
        String function = "deleteMessage";
        String[] args = {id, messageID};
        String[] status = translator.query(new Query(object, function, args));
        if (status[0] == "INVALID") {
            // handled by caller method.
            return "INVALID";
        }
        return "SUCCESS";
    }

    public String[] getMessageHistory(String id) {
        String object = "messageManager";
        String function = "getMessageHistory";
        String[] args = {id};
        String[] result = translator.query(new Query(object, function, args));
        return result;
    }
}
