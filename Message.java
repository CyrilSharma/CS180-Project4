import java.time.Instant;

public class Message {
    
    private String message;
    private String sender;
    private String recipient;
    private Instant timeStamp;
    private String store;
    private String messageID;

    public Message(String message, String messageID, String sender, String recipient, Instant timeStamp, String store) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.timeStamp = timeStamp;
        this.store = store;
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public String getStore() {
        return store;
    }

    public String getMessageID() {
        return messageID;
    }
}
