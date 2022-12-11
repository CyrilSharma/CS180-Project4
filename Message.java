import java.io.Serializable;
import java.time.Instant;
/**
 * Project 5 -> Message
 *
 * class handles the message timestamp sender and messageID
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
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
    /**
     * returns the message instance field
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }
    /**
     * returns the sender instance field
     *
     * @return sender
     */
    public String getSender() {
        return sender;
    }
    /**
     * returns the recipient instance field
     *
     * @return recipient
     */
    public String getRecipient() {
        return recipient;
    }
    /**
     * returns the time instance filed
     *
     * @return time
     */
    public Instant getTimeStamp() {
        return timeStamp;
    }
    /**
     * returns the store instance field
     *
     * @return store
     */
    public String getStore() {
        return store;
    }
    /**
     * returns the messageID instance field
     *
     * @return messageID
     */
    public String getMessageID() {
        return messageID;
    }
    /**
     * returns true or false if two objects are equal
     *
     * @return True or False
     */
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
