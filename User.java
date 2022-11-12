import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    //Instance fields
    private String email;
    private boolean hasAccount;
    private String password;
    private String role;
    private Database db;
    private ArrayList<String> stores;
    private MessageManager manager;

    private String id; //user's personal ID
    private String splitter = "-----";
    private int splitVal = 0;
    //creating an account
    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.hasAccount = true;
        this.manager = new MessageManager("UserDatabase.txt", "history");
        db = new Database("UserDatabase.txt");
        stores = new ArrayList<>();
        this.id = db.get("email", this.email).get("id");
    }

    //deleting an account
    public void deleteAccount() {
        this.email = null;
        this.password = null;
        this.role = null;
        this.hasAccount = false;
    }

    //edit an account
    public void editAccount(String username, String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
    //checks if the account exists
    public boolean accountExists() {
        return this.hasAccount;
    }

    //This method allows the user, if customer, to view the list of stores
    //if the customer is not blocked, they can view the list of stores
    public void viewStores() throws InvalidUserException, IOException {
        boolean blocked = false;
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", "customer");
        for (HashMap<String, String> sellerBlocked : getSellers) {
            if (sellerBlocked.get("blocked").contains(id)) {
                blocked = true;
            }
        }
        if (this.role.toLowerCase().equals("customer") && !blocked) {
            BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
            }
            br.close();
        } else if (blocked) {
            throw new InvalidUserException(this.id + "has been blocked");
        }
    }
    //Customer can select a store and then send a message to the associated seller
    //the customer can go ahead and select the stores if they are not blocked
    //an InvalidUser message will be thrown if the user is blocked
    public void selectStore(String store, String message) throws InvalidUserException, IOException {
        boolean blocked = false;
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", "customer");
        for (HashMap<String, String> sellerBlocked : getSellers) {
            if (sellerBlocked.get("blocked").contains(id)) {
                blocked = true;
            }
        }
        if (this.role.toLowerCase().equals("customer") && !blocked) {
            ArrayList<String> stores = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
            String line = br.readLine();
            while (line != null) {
                stores.add(line);
                line = br.readLine();
            }
            for (int i = 0; i < stores.size(); i++) {
                if (stores.get(i).contains(store)) {
                    String seller = stores.get(i).split("-")[1];
                    manager.messageUser(this.email, seller, message);
                }
            }
        } else if (blocked) {
            throw new InvalidUserException(this.id + "has been blocked");
        }
    }

    //Implementing Seller
    //contains the blocked functionality where the user cannot input the store if the seller is blocked
    public void addStores(String store) throws InvalidUserException {
        if (this.role.toLowerCase().equals("seller")) {
            boolean blocked = false;
            ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", "seller");
            for (HashMap<String, String> sellerBlocked : getSellers) {
                if (sellerBlocked.get("blocked").contains(id)) {
                    blocked = true;
                }
            }
            if (!blocked) {
                try {
                    File f = new File("stores.txt");
                    FileOutputStream fos = new FileOutputStream(f, false);
                    PrintWriter pw = new PrintWriter(fos);
                    String sellerID = db.get("email", this.email).get("id");
                    if (splitVal == 0) {
                        pw.write(sellerID + splitter + "\n");
                        splitVal += 1;
                    }
                    pw.write(store + "\n");
                    stores.add(store);
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (blocked) {
                throw new InvalidUserException(this.id + "has been blocked");
            }
        }
    }
    //Seller views list of customers that they can send messages to
    public void viewCustomers() throws InvalidUserException { //blocked
        boolean blocked = false;
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", "seller");
        for (HashMap<String, String> sellerBlocked : getSellers) {
            if (sellerBlocked.get("blocked").contains(id)) {
                blocked = true;
            }
        }
        if (this.role.toLowerCase().equals("seller") && !blocked) {
            ArrayList<String> customers = manager.getNames("Customer");
            for (String customer : customers) {
                System.out.println(customer);
            }
        } else if (blocked) {
            throw new InvalidUserException(this.id + "has been blocked");
        }
    }
    //get list of stores
    public ArrayList<String> getStores() {
        return stores;
    }

    //Seller can choose a customer to send a message to
    public void selectCustomer(String recipient, String message) throws InvalidUserException, IOException {
        boolean blocked = false;
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", "seller");
        for (HashMap<String, String> sellerBlocked : getSellers) {
            if (sellerBlocked.get("blocked").contains(id)) {
                blocked = true;
            }
        }
        if (this.role.toLowerCase().equals("seller") && !blocked) {
            ArrayList<String> customers = manager.getNames("Customer");
            for (String customer : customers) {
                if (email.equals(customer)) {
                    manager.messageUser(this.email, recipient, message);
                }
            }
        } else if (blocked) {
            throw new InvalidUserException(this.id + "has been blocked");
        }
    }
}
