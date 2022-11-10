import java.io.*;
import java.util.ArrayList;

public class User {
    private String email;

    private boolean hasAccount;
    private String password;
    private String role;
    private Database db;
    private ArrayList<String> stores;
    private MessageManager manager;
    //creating an account
    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.hasAccount = true;
        this.manager = new MessageManager();
        db = new Database("UserDatabase.txt");
        stores = new ArrayList<>();
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

    public void viewStores() throws InvalidUserException, IOException {
        //Will print store and seller
        if (this.role.toLowerCase().equals("customer")) {
            BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
            }
        }
    }
    //Customer can select a store and then send a message to the associated seller
    public void selectStore(String store, String message) throws InvalidUserException, IOException {
        if (this.role.toLowerCase().equals("customer")) {
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
        }
    }

    //Implementing Seller
    public void addStores(String store) {
        if (this.role.toLowerCase().equals("seller")) {
            try {
                File f = new File("stores.txt");
                FileOutputStream fos = new FileOutputStream(f, false);
                PrintWriter pw = new PrintWriter(fos);
                pw.write(store + "-" + this.email + "\n");
                stores.add(store);
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Seller views list of customers that they can send messages to
    public void viewCustomers() throws InvalidUserException {
        if (this.role.toLowerCase().equals("seller")) {
            ArrayList<String> customers = manager.getNames("Customer");
            for (String customer : customers) {
                System.out.println(customer);
            }
        }
    }
    //get list of stores
    public ArrayList<String> getStores() {
        return stores;
    }

    //Seller can choose a customer to send a message to
    public void selectCustomer(String recipient, String message) throws InvalidUserException, IOException {
        if (this.role.toLowerCase().equals("seller")) {
            ArrayList<String> customers = manager.getNames("Customer");
            for (String customer : customers) {
                if (email.equals(customer)) {
                    manager.messageUser(this.email, recipient, message);
                }
            }
        }
    }
}
