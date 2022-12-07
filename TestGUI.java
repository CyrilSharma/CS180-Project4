import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TestGUI {
    public static void main(String[] args) {
        //LogInGUI gui = new LogInGUI();
        ArrayList<String> users = new ArrayList<>();
        ArrayList<String> blockedUsers = new ArrayList<>();
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
        stores.put("str", "someone@gmail.com");
        Translator tr = new Translator();

        try {
            //tr.query(new Query("Database", "verify", new String[]{"avadakedavera@gmail.com", "password"}));
            tr.query(new Query("Database", "verify", new String[]{"palpatine@senate.gov", "password"}));
            //DashboardGUI gui = new DashboardGUI(frame, stores, tr.get("email", "bob@gmail.com"));
            //gui.show();
            PeopleView gui = new PeopleView(frame, tr.get("email", "palpatine@senate.gov"));
            gui.show();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


//        LogInGUI mainMenu = new LogInGUI(users);
//        mainMenu.show();

        //MessageGUI message = new MessageGUI(frame,"view", "cs183@purdue.edu", "temp", "store0");
        //message.show();
       // BlockGUI blockGUI = new BlockGUI("cs186@gmail.com", users, blockedUsers);
       // blockGUI.show();
    }
}
