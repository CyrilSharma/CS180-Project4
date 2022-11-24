import java.util.ArrayList;

public class TestGUI {
    public static void main(String[] args) {
        LogInGUI gui = new LogInGUI();
        ArrayList<String> users = new ArrayList<>();
        users.add("helloworld@gmail.com");
        users.add("hmmmm@gmail.com");
        users.add("cs184@gmail.com");
        users.add("cs185@gmail.com");
        users.add("cs186@gmail.com");
        users.add("cs187@gmail.com");
        users.add("cs188@gmail.com");users.add("cs189@gmail.com");
        users.add("cs190@gmail.com");
        users.add("cs191@gmail.com");
        users.add("cs144@gmail.com");users.add("cs112@gmail.com");
        users.add("cs199@gmail.com");
        users.add("cs144@gmail.com");
        users.add("cs166@gmail.com");
        users.add("cs177@gmail.com");
        users.add("cs133@gmail.com");
        users.add("cs111@gmail.com");
        users.add("cs1222@gmail.com");
        users.add("cs15567@gmail.com");
        users.add("cs17567@gmail.com");
        users.add("cs181@gmail.com");
        users.add("cs1811@gmail.com");
        users.add("cs18111@gmail.com");
        users.add("cs1811111@gmail.com");

        //PeopleView gui = new PeopleView(users, Role.Seller);
        //gui.show();

        LogInGUI mainMenu = new LogInGUI(users);
        mainMenu.show();
    }
}
