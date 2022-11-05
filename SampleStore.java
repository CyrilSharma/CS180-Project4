import java.util.*;
//sample store class
public class SampleStore {
    private HashMap<SampleCustomer, Integer> messageSent;
    private HashMap<SampleCustomer, Integer> messageReceived;
    private String mostCommonWord;
    private String name;
    private SampleSeller owner;

    public SampleStore(String word, String name, SampleSeller seller, HashMap<SampleCustomer,
                       Integer> received, HashMap<SampleCustomer, Integer> sent) {
        messageSent = sent;
        messageReceived = received;
        mostCommonWord = word;
        this.name = name;
        owner = seller;

    }

    public String getName() {
        return name;
    }

    public HashMap<SampleCustomer, Integer> getMessageSentMap() {
        return messageSent;
    }
    public HashMap<SampleCustomer, Integer> getMessageReceivedMap() {
        return messageReceived;
    }
    public String getMostCommonWord() {
        return mostCommonWord;
    }
    //testing method
    public void sendMessage(SampleCustomer user) {
        for (SampleCustomer i : messageSent.keySet()) {
            if (i.getUsername().equals(user.getUsername())) {
                messageSent.put(user, messageSent.get(user) + 1);
            }
        }
    }
    public void setMostCommonWord(String word) {
        mostCommonWord = word;
    }
    public void setSentMessage(SampleCustomer user, int messages) {
        messageSent.put(user, messages);
    }
    public void setReceiveMessage(SampleCustomer user, int messages) {
        messageReceived.put(user, messages);
    }
    public String getOwnerName() {
        return owner.getUsername();
    }
    //testing method
    public void receiveMessage(SampleCustomer user) {
        for (SampleCustomer i : messageReceived.keySet()) {
            if (i.getUsername().equals(user.getUsername())) {
                messageReceived.put(user, messageReceived.get(user) + 1);
            }
        }
    }
    public int getUserMessageSent(SampleCustomer user) {
        //System.out.println(messageSent.size());
        //System.out.println(messageSent.keySet().size());
        for (SampleCustomer i : messageSent.keySet()) {
            if (i.getUsername().equals(user.getUsername())) {
                return messageSent.get(user);
            }
        }
        return 0;
    }
    public int getUserMessageReceived(SampleCustomer user) {
        for (SampleCustomer i : messageReceived.keySet()) {
            if (i.getUsername().equals(user.getUsername())) {
                return messageReceived.get(user);
            }
        }
        return 0;
    }
}
