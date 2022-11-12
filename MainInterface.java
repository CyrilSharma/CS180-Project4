import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class MainInterface {

    public static void main(String[] args) throws InvalidUserException, IOException {
        Database db = new Database("UserDatabase.txt");
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
                    HashMap<String, String> acct = db.get("email", email);
                    User user = new User(email, password, acct.get("role"));
                    String MESSAGEPROMPT = "Would you like to..." +
                            "\n1. Message a user" +
                            "\n2. Edit a message" +
                            "\n3. Delete a message" +
                            "\n4. Block a user" +
                            "\n5. Add a store" +
                            "\n6. Dashboard" +
                            "\n7. open filter" +
                            "\n8. Exit" +
                            "\n" + "(1-8)\n";
                    System.out.println(MESSAGEPROMPT);
                    int userAction;
                    userAction = scan.nextInt();
                    scan.nextLine();
                    while (userAction < 1 || userAction > 8 ) {
                        System.out.println("INVALID INPUT");
                        userAction = scan.nextInt();
                        scan.nextLine();
                    }
                    if (userAction == 1) {
                        //message user
                        String MESSAGEPROMPT2 = "";
                        if (acct.get("role").toLowerCase() == "customer") {
                            //option to view available stores
                            System.out.println("Would you like to view a list of the available stores? (Y/N)");
                            String storeList = "";
                            storeList = scan.nextLine();
                            while (!storeList.equals("Y") && !storeList.equals("N")) {
                                System.out.println("Invalid input, try again.");
                                System.out.println("Would you like to view a list of the available stores? (Y/N)");
                                storeList = scan.nextLine();
                            }
                            if (storeList.equals("Y")) {
                                try {
                                    user.viewStores();
                                } catch (IOException e) {
                                    System.out.println("Could not perform action");
                                }
                            }
                            String contact = "";
                            String message = "";
                            //choose store
                            System.out.println("Which store would you like to contact?");
                            contact = scan.nextLine();
                            //write message
                            System.out.println("What message would you like to send?");
                            message = scan.nextLine();
                            user.selectStore(contact, message);
                        } else {
                            //option to view list of customers
                            System.out.println("Would you like to view a list of customers? (Y/N)");
                            String storeList = "";
                            storeList = scan.nextLine();
                            while (!storeList.equals("Y") && !storeList.equals("N")) {
                                System.out.println("Invalid input, try again.");
                                System.out.println("Would you like to view a list of customers? (Y/N)");
                                storeList = scan.nextLine();
                            }
                            if (storeList.equals("Y")) {
                                user.viewCustomers();
                            }
                            String contact = "";
                            String message = "";
                            //choose customer
                            System.out.println("Which customer would you like to contact?");
                            contact = scan.nextLine();
                            //write message
                            System.out.println("What message would you like to send?");
                            message = scan.nextLine();
                            user.selectCustomer(contact, message);
                        }
                    } else if (userAction == 2) {
                        //edit message
                        String view = "";
                        System.out.println("Would you like to see your message history to find the conversation? (Y/N)");
                        view = scan.nextLine();
                        while (!view.equals("Y") && !view.equals("N")) {
                            System.out.println("Invalid input, try again.");
                            System.out.println("Would you like to see your message history to find the conversation? (Y/N)");
                            view = scan.nextLine();
                        }
                        if (view.equals("Y")) {
                            messageManager.getPersonalHistory(acct.get("ID"));
                        }
                        String recipient = "";
                        //Should this be changed to somehow take seller name and get convo?
                        //message recipient email
                        System.out.println("Who did you send the message to?");
                        recipient = scan.nextLine();
                        int messageNum;
                        //message number
                        System.out.println("Which message (number) would you like to edit?");
                        messageNum = scan.nextInt();
                        scan.nextLine();
                        String newMessage = "";
                        //what the new message will be
                        System.out.println("What would you like to change the message to?");
                        newMessage = scan.nextLine();
                        messageManager.editMessage(acct.get("ID"), db.get("role", recipient).get("email"), newMessage, String.valueOf(messageNum));
                    } else if (userAction == 3) {
                        //delete message
                        String view = "";
                        //option to view message history
                        System.out.println("Would you like to see your message history to find the conversation? (Y/N)");
                        view = scan.nextLine();
                        while (!view.equals("Y") && !view.equals("N")) {
                            System.out.println("Invalid input, try again.");
                            System.out.println("Would you like to see your message history to find the conversation? (Y/N)");
                            view = scan.nextLine();
                        }
                        if (view.equals("Y")) {
                            messageManager.getPersonalHistory(acct.get("ID"));
                        }
                        String recipient = "";
                        //message recipient email
                        System.out.println("Who did you send the message to?");
                        recipient = scan.nextLine();
                        int messageNum;
                        //message number
                        System.out.println("Which message (number) would you like to delete?");
                        messageNum = scan.nextInt();
                        scan.nextLine();
                        messageManager.deleteMessage(acct.get("ID"), db.get("role", recipient).get("email"), String.valueOf(messageNum));
                    } else if (userAction == 4) {
                        //block user
                        //option to see list of customers or stores (dep on role) to see who to block
                        if (acct.get("role").toLowerCase() == "customer") {
                            System.out.println("Would you like to view a list of stores?");
                            System.out.println("Would you like to view a list of customers? (Y/N)");
                            String storeList = "";
                            storeList = scan.nextLine();
                            while (!storeList.equals("Y") && !storeList.equals("N")) {
                                System.out.println("Invalid input, try again.");
                                System.out.println("Would you like to view a list of customers? (Y/N)");
                                storeList = scan.nextLine();
                            }
                            if (storeList.equals("Y")) {
                                user.viewCustomers();
                            }
                        } else {
                            System.out.println("Would you like to view a list of customers?");
                        }

                    } else if (userAction == 5) {
                        if (acct.get("role").toLowerCase() == "customer") {
                            System.out.println("You cannot do this, you are a customer!");
                        }
                    } else if (userAction == 6) {
                        Dashboard dashboard = new Dashboard(email, "");
                        dashboard.readDatabase();
                        dashboard.presentDashboard();
                    } else if (userAction == 7) {
                        Filter f = new Filter(email);
                        f.presentFilterMenu();
                    } else if (userAction == 8){
                        //exit
                    }
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
            if (role.toLowerCase() == "seller") {
                System.out.println("How many stores would you like to create?");
                int numStores = scan.nextInt();
                for (int i = 0; i < numStores; i++) {
                    String storeName = "";
                    System.out.println("What is the name of store " + i + "?");
                    storeName = scan.nextLine();
                    account.addStores(storeName);
                    System.out.println(storeName + "has been created!");
                }
            }
        } else if (resp == 3) {
            //deleting account
            System.out.println("Please enter your email: ");
            String email = scan.nextLine();
            db.remove(email);
            System.out.println("Account deleted!");
        } else if (resp == 4) {
            //Exit program
            //should this print at the end no matter what? if so, change to separate if loop at end
            System.out.println("Have a nice rest of your day!");
        }
    }
}
