import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

public class TestGUI {
    public static void main(String[] args) {
        //LogInGUI gui = new LogInGUI();
        ArrayList<String> users = new ArrayList<>();
        ArrayList<String> blockedUsers = new ArrayList<>();
        ArrayList<String> invisibleUsers = new ArrayList<>();
        users.add("helloworld@gmail.com");
        users.add("hmmmm@gmail.com");
        users.add("cs184@gmail.com");
        users.add("cs185@purdue.edu");
        users.add("cs186@gmail.com");
        users.add("cs187@gmail.com");
        users.add("cs188@purdue.edu");users.add("cs189@purdue.edu");
        users.add("cs190@gmail.com");
        users.add("cs191@gmail.com");
        users.add("jacob@gmail.com");users.add("cs112@gmail.com");
        users.add("cs199@gmail.com");
        users.add("cs144@gmail.com");
        users.add("cs166@gmail.com");
        users.add("cs177@purdue.edu");
        users.add("cs133@gmail.com");
        users.add("cs111@gmail.com");
        users.add("cs1222@purdue.edu");
        users.add("cs15567@gmail.com");
        users.add("cs17567@purdue.edu");
        users.add("cs181@gmail.com");
        users.add("cs1811@gmail.com");
        users.add("cs18111@gmail.com");
        users.add("cs1811111@gmail.com");
        blockedUsers.add("cs186@gmail.com");
        invisibleUsers.add("cs186@gmail.com");

        //LogInGUI gui = new LogInGUI(users);
        JFrame frame = new JFrame("Turkey Shop");
        frame.setSize(600,540);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
       // MainMenuGUI main = new MainMenuGUI(frame, users, Role.Seller);
        //main.show();
        HashMap<String, String> stores = new HashMap<>();
        stores.put("Store1", "helloworld@gmail.com");
        Translator tr = new Translator();
        try {
            PeopleView gui = new PeopleView(frame, users, tr.get("email", "bob@gmail.com"), stores);
            gui.show();
        } catch (Exception e) {

        }


//        LogInGUI mainMenu = new LogInGUI(users);
//        mainMenu.show();

        //MessageGUI message = new MessageGUI(frame,"view", "cs183@purdue.edu", "temp", "store0");
        //message.show();
       BlockGUI blockGUI = new BlockGUI("cs186@gmail.com", users, blockedUsers, invisibleUsers);
       blockGUI.show();
    }
}
