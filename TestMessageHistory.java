import java.io.IOException;

public class TestMessageHistory {
    public static void main(String[] args) {
        MessageManager mm = new MessageManager("UserDatabase.txt");
        try {
            System.out.println(mm.readTextFromFile("message.txt"));
            mm.messagesToCSV("fjkajdjdj", new String[]{"jkxjksksk"});
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
