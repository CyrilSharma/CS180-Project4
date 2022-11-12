import java.io.IOException;
import java.io.*;
import java.util.*;

public class TestMessageHistory {
    public static void main(String[] args) {
        MessageManager mm = new MessageManager("UserDatabase.txt");
        Database db = new Database("UserDatabase.txt");
        //MessageInterface.message(new Scanner(System.in), mm, db, "jkxjksksk");
        MessageInterface.viewMessageHistory(new Scanner(System.in), "jkxjksksk", db, mm);
    }
}
