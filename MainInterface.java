import java.util.HashMap;
import java.util.Scanner;

public class MainInterface {
    public static void main(String[] args) throws InvalidUserException {
        Database db = new Database("Database.txt");
        String PROMPT = "Would you like to..." +
                "\n1. Login" +
                "\n2. Create an account" +
                "\n3. Delete an account" +
                "\n4. Exit\n";

        Scanner scan = new Scanner(System.in);
        //Welcome message
        System.out.println("Welcome to the Turkey Store!");
        System.out.println(PROMPT);
        int resp = -1;
        do {
            try {
                resp = scan.nextInt();
                scan.nextLine();
                if (resp < 1 || resp > 4) {
                    System.out.println("Input invalid, try again.");
                    System.out.println(PROMPT);
                    resp = -1;
                }
            } catch (Exception e) {
                System.out.println("Input invalid, try again.");
                System.out.println(PROMPT);
            }
        } while (resp == -1);

        MessageManager messageManager = new MessageManager("UserDatabase.txt");
        if (resp == 1) { //Login
            boolean loggedIn = false;
            String email = "";
            String password = "";
            while (!loggedIn) {
                System.out.println("Please enter your email: ");
                email = scan.nextLine();
                System.out.println("Please enter your password: ");
                password = scan.nextLine();
                if (db.verify(email, password)) {
                    loggedIn = true;
                    System.out.println("Login successful!");
                    HashMap<String, String> acct = db.get("role", email);
                    User user = new User(email, password, acct.get("email"));
                } else {
                    System.out.println("Incorrect username or password. Please try again");
                }
            }

        } else if (resp == 2) { //Creating Acct
            System.out.println("Please enter your email: ");
            String email = scan.nextLine();
            System.out.println("Please enter your password: ");
            String password = scan.nextLine();

            boolean roleValid = false;
            String role = "";
            while (!roleValid) {
                System.out.println("Please enter your role (Seller/Customer): ");
                role = scan.nextLine();

                if (role.toLowerCase().equals("seller") || role.toLowerCase().equals("customer")) {
                    roleValid = true;
                }
            }
            User account = new User(email, password, role);
            db.add(email, password, Role.valueOf(role));
            System.out.println("Account created!");
        } else if (resp == 3) {
            //deleting account
            System.out.println("Please enter your email: ");
            String email = scan.nextLine();
            db.remove(email);
            System.out.println("Account deleted!");
        } else if (resp == 4) {
            //Exit program
            System.out.println("Have a nice rest of your day!");
        }
    }
}
