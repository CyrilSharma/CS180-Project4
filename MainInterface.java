import java.time.Instant;
import java.util.*;

//TODO: Add javadoc
public class MainInterface {

    public static void main(String[] args) {
        // Lets you switch out defaults with command line arguments.
        // Testing purposes only.
        String databasePath = "UserDatabase.txt";
        String historyPath = "history";
        if (args.length == 2) {
            databasePath = args[0];
            historyPath = args[1];
        }

        System.out.println("Welcome to the Turkey Store!\n" + 
            "Here at the turkey store, we offer every variety of turkey imaginable!\n" +
            "Feel free to contact any of our reputable sellers to learn about stores, \n" +
            "that you can buy our wonderful products from!\n"
        );

        Database db = new Database(databasePath);
        Scanner scan = new Scanner(System.in);
        boolean loop1 = true;
        boolean loop2 = false;
        boolean filter = false;
        while (loop1) {

            String prompt = "Would you like to..." +
                    "\n1. Login" +  
                    "\n2. Create an account" +
                    "\n3. Exit";
                    
            int resp = -1;
            System.out.println(prompt);
            do {
                try {
                    resp = Integer.parseInt(scan.nextLine());
                    if (resp < 1 || resp > 3) {
                        System.out.println("Input invalid, try again.");
                        System.out.println(prompt);
                        resp = -1;
                    }
                } catch (Exception e) {
                    System.out.println("Input invalid, try again.");
                    System.out.println(prompt);
                    resp = -1;
                }
            } while (resp == -1);

            MessageManager messageManager = new MessageManager(db, historyPath);
            if (resp == 1) { //Login
                boolean loggedIn = false;
                String email = "";
                String password = "";
                while (!loggedIn) {
                    System.out.println("Please enter your email (type back to go back):");
                    email = scan.nextLine();
                    if (email.equals("back")) {
                        break;
                    }
                    System.out.println("Please enter your password (type back to go back):");
                    password = scan.nextLine();
                    if (password.equals("back")) {
                        break;
                    }
                    if (db.verify(email, password)) {
                        loggedIn = true;
                        loop2 = true;
                        System.out.println("Login successful!");
                        HashMap<String, String> acct = db.get("email", email);
                        User user = new User(email, password, acct.get("role"), messageManager, db);
                        MessageInterface.missedMessages(scan, acct.get("id"), db, messageManager);
                        Filter f = new Filter(email, db);
                        try {
                            db.modify(email, "lastOnline", Instant.now().toString());
                        } catch (InvalidUserException | InvalidKeyException e) {
                            System.out.println("Something went wrong trying to log you in");
                        }
                        while (loop2) {
                            String messagePrompt = "Would you like to..." +
                                    "\n1. Message a user" +
                                    "\n2. View messages" +
                                    "\n3. Edit a message" +
                                    "\n4. Delete a message" +
                                    "\n5. Export conversations" +
                                    "\n6. Block a user" +
                                    "\n7. Unblock a user" +
                                    "\n8. View stores" +
                                    "\n9. View customers" +
                                    "\n10. Add a store" +
                                    "\n11. Dashboard" +
                                    "\n12. Open Filter" +
                                    "\n13. Delete your account" +
                                    "\n14. Exit";
                            System.out.println(messagePrompt);
                            filter = f.getStatus();
                            int userAction = -1;
                            do {
                                try {
                                    userAction = Integer.parseInt(scan.nextLine());
                                } catch (NumberFormatException e) {
                                    userAction = -1;
                                }
                                if (userAction < 1 || userAction > 14) {
                                    System.out.println("Input invalid, try again.");
                                    System.out.println(messagePrompt);
                                }
                            } while (userAction < 1 || userAction > 14);
                            if (userAction == 1) {
                                //message user
                                if (acct.get("role").toLowerCase().equals("customer")) {
                                    user.viewStores();
                                } else if (acct.get("role").equals(Role.Seller.toString())) {
                                    user.viewCustomers();
                                }
                                MessageInterface.message(scan, messageManager, db, acct.get("id"));
                            } else if (userAction == 2) {
                                //view message history
                                MessageInterface.viewMessageHistory(scan, acct.get("id"), db, messageManager, 
                                    filter, f, true);
                            } else if (userAction == 3) {
                                //edit a message
                                ArrayList<HashMap<String, String>> messages = MessageInterface.viewMessageHistory(scan, 
                                    acct.get("id"), db, messageManager, filter, f, true);
                                if (messages == null) {
                                    continue;
                                }
                                int messageNum = -1;
                                //message number
                                do {
                                    System.out.println("Which message number would you like to edit?");
                                    try {
                                        messageNum = Integer.parseInt(scan.nextLine());
                                        if (messageNum < 1 || messageNum > messages.size()) {
                                            messageNum = -1;
                                            System.out.println("Please choose one of the messages listed above");
                                        }
                                    } catch (NumberFormatException e) {
                                        messageNum = -1;
                                        System.out.println("Enter a number please");
                                    }
                                } while (messageNum == -1);
                                HashMap<String, String> selectedMessage = messages.get(messageNum - 1);
                                String newMessage = "";
                                //what the new message will be
                                System.out.println("What would you like to change the message to?");
                                newMessage = scan.nextLine();
                                messageManager.editMessage(acct.get("id"), selectedMessage.get("recipient"), 
                                    newMessage, selectedMessage.get("messageNum"));
                                System.out.println("Message edited");
                            } else if (userAction == 4) {
                                //delete message
                                ArrayList<HashMap<String, String>> messages = MessageInterface.viewMessageHistory(scan, 
                                    acct.get("id"), db, messageManager, filter, f, true);
                                if (messages == null) {
                                    continue;
                                }
                                int messageNum = -1;
                                //message number
                                do {
                                    System.out.println("Which message number would you like to delete?");
                                    try {
                                        messageNum = Integer.parseInt(scan.nextLine());
                                        if (messageNum < 1 || messageNum > messages.size()) {
                                            messageNum = -1;
                                            System.out.println("Please choose one of the messages listed above");
                                        }
                                    } catch (NumberFormatException e) {
                                        messageNum = -1;
                                        System.out.println("Enter a number please");
                                    }
                                } while (messageNum == -1);
                                HashMap<String, String> selectedMessage = messages.get(messageNum - 1);
                                messageManager.deleteMessage(acct.get("id"), selectedMessage.get("recipient"), 
                                    selectedMessage.get("messageNum"));
                                System.out.println("Message deleted");
                            } else if (userAction == 5) {
                                //export conversations
                                MessageInterface.exportConversations(scan, acct.get("id"), db, messageManager);
                            } else if (userAction == 6) {
                                //block user
                                //option to see list of customers or stores (dep on role) to see who to block
                                if (acct.get("role").toLowerCase().equals("customer")) {
                                    user.viewStores();
                                } else if (acct.get("role").equals(Role.Seller.toString())) {
                                    user.viewCustomers();
                                }
                                System.out.println("Who would you like to block?");
                                String userToBlock = "";
                                userToBlock = scan.nextLine();
                                if (db.get("email", userToBlock) == null) {
                                    System.out.println("That is not a valid email");
                                    continue;
                                }
                                System.out.println("Would you like to block them or become" 
                                    + " invisble to them (block/invisible)?");
                                boolean invisible = scan.nextLine().toLowerCase().equals("invisible");
                                try {
                                    db.block(email, userToBlock, invisible);
                                } catch (InvalidUserException e) {
                                    System.out.println(e.getMessage());
                                    continue;
                                }
                                if (invisible) {
                                    System.out.println("You are invisible to " + userToBlock);
                                } else {
                                    System.out.println(userToBlock + " has been blocked!");
                                }
                            } else if (userAction == 7) {
                                //unblock a user
                                if (acct.get("role").equals(Role.Customer.toString())) {
                                    user.viewStores();
                                } else {
                                    user.viewCustomers();
                                }
                                System.out.println("Who would you like to unblock or become visible to?");
                                String name = scan.nextLine();
                                if (db.get("email", name) == null) {
                                    System.out.println("That is not a valid email");
                                    continue;
                                }
                                System.out.println("Would you like to unblock them or become visible" 
                                    + " to them (unblock/visible)");
                                boolean visible = scan.nextLine().toLowerCase().equals("visible");
                                try {
                                    db.unblock(email, name, visible);
                                } catch (InvalidUserException e) {
                                    System.out.println(e.getMessage());
                                    continue;
                                }
                                if (visible) {
                                    System.out.println("You are visible to " + name);
                                } else {
                                    System.out.println(name + " has been unblocked!");
                                }
                            } else if (userAction == 8) {
                                //view stores
                                if (acct.get("role").equals(Role.Customer.toString())) {
                                    user.viewStores();
                                } else if (acct.get("role").equals(Role.Seller.toString())) {
                                    System.out.println("You cannot do this, you are a seller!");
                                }
                            } else if (userAction == 9) {
                                //view customers
                                if (acct.get("role").equals(Role.Seller.toString())) {
                                    user.viewCustomers();
                                } else if (acct.get("role").equals(Role.Customer.toString())) {
                                    System.out.println("You cannot do this, you are a customer!");
                                }
                            } else if (userAction == 10) {
                                //create stores
                                if (acct.get("role").toLowerCase().equals("customer")) {
                                    System.out.println("You cannot do this, you are a customer!");
                                } else if (acct.get("role").equals(Role.Seller.toString())) {
                                    String storeName = "";
                                    System.out.println("What is the name of the store you would like to add?");
                                    storeName = scan.nextLine();
                                    try {
                                        user.addStores(storeName);
                                    } catch (InvalidUserException e) {
                                        e.printStackTrace();
                                        System.out.println(e.getMessage());
                                        continue;
                                    }
                                    System.out.println(storeName + " has been created!");
                                }
                            } else if (userAction == 11) {
                                //view statistics
                                Dashboard dashboard = new Dashboard(email, "", db);
                                dashboard.readDatabase();
                                dashboard.printMyStatistic();
                                dashboard.presentDashboard(scan);
                            } else if (userAction == 12) {
                                f.presentFilterMenu(scan, filter);
                            } else if (userAction == 13) {
                                //delete account
                                System.out.println("Are you sure you want to delete your account (Y/N)");
                                if (scan.nextLine().toUpperCase().equals("Y")) {
                                    System.out.println("Confirm by entering your password:");
                                    if (db.verify(email, scan.nextLine())) {
                                        try {
                                            db.remove(email);
                                        } catch (InvalidUserException e) {
                                            System.out.println(e.getMessage());
                                            continue;
                                        }
                                        System.out.println("Account deleted!");
                                        loop2 = false;
                                        break;
                                    }
                                }
                            } else if (userAction == 14) {
                                //Exit? (Could be a go back to start button, would need while around everything then)
                                loop2 = false;
                            } else {
                                System.out.println("Incorrect username or password. Please try again");
                            }
                            try {
                                db.modify(email, "lastOnline", Instant.now().toString());
                            } catch (InvalidUserException | InvalidKeyException e) {
                                System.out.println("Something went wrong trying to log you out");
                            }
                        }
                    } else {
                        System.out.println("Sorry, the email or password you entered is incorrect");
                    }
                }
            } else if (resp == 2) { //Creating Acct
                String email;
                do {
                    System.out.println("Please enter your email: ");
                    email = scan.nextLine();
                    if (!db.validate(email, "email") || db.get("email", email) != null) {
                        System.out.println("Please enter a valid email");
                        email = "";
                    }
                } while (!db.validate(email, "email"));
                String password;
                do {
                    System.out.println("Please enter your password:");
                    password = scan.nextLine();
                    if (!db.validate(password, "password")) {
                        System.out.println("That password is not valid. Please do not use special characters");
                    }
                } while (!db.validate(password, "password"));
                boolean roleValid = false;
                String role = "";
                Role rolething = null;
                while (!roleValid) {
                    System.out.println("Please enter your role (Seller/Customer):");
                    role = scan.nextLine();

                    if (role.toLowerCase().equals("seller")) {
                        roleValid = true;
                        rolething = Role.Seller;
                    }
                    if (role.toLowerCase().equals("customer")) { 
                        roleValid = true;
                        rolething = Role.Customer;
                    }
                }
                try {
                    db.add(email, password, rolething);
                } catch (InvalidUserException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                User account = new User(email, password, role, messageManager, db);
                System.out.println("Account created!");
                if (role.toLowerCase().equals("seller")) {
                    int numStores = -1;
                    do {
                        System.out.println("How many stores would you like to create?");
                        try {
                            numStores = Integer.parseInt(scan.nextLine());
                        } catch (NumberFormatException e) {
                            numStores = -1;
                            System.out.println("Enter a number");
                        }
                        if (numStores <= 0) {
                            System.out.println("You must make at least one store!");
                        }
                    } while (numStores <= 0);
                    //scan.nextLine();
                    for (int i = 1; i <= numStores; i++) {
                        String storeName = "";
                        System.out.println("What is the name of store " + i + "?");
                        storeName = scan.nextLine();
                        try {
                            account.addStores(storeName);
                        } catch (InvalidUserException e) {
                            System.out.println(e.getMessage());
                            continue;
                        }
                        System.out.println(storeName + " has been created!");
                    }
                }
            } else if (resp == 3) {
                //Exit program
                //should this print at the end no matter what? if so, change to separate if loop at end
                System.out.println("Thank you for shopping at the turkey store!\n" + 
                    "Have a nice rest of your day!");
                loop1 = false;
            }
        }
    }
}