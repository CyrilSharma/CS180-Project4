import java.io.Serializable;
import java.time.Instant;

public class Message implements Serializable {
    
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Message) {
            Message m = (Message) o;
            if (m == this) {
                return true;
            }
            if (m.getMessage() == message || m.getMessage().equals(message)) {
                if (m.getMessageID() == messageID || m.getMessageID().equals(messageID)) {
                    if (m.getRecipient() == recipient || m.getRecipient().equals(recipient)) {
                        if (m.getSender() == sender || m.getSender().equals(sender)) {
                            if (m.getStore() == store || m.getStore().equals(store)) {
                                if (m.getTimeStamp() == timeStamp || m.getTimeStamp().equals(timeStamp)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
