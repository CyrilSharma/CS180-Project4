import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    //Instance fields
    private String email;
    private boolean hasAccount;
    private String role;
    private Database db;
    private ArrayList<String> stores;

    private String id; //user's personal ID
    private String splitter = "-----";
    private int splitVal = 0;
    
    //creating an account
    public User(String email, String password, String role, MessageManager manager, Database db) {
        this.email = email;
        this.role = role;
        this.hasAccount = true;
        this.db = db;
        stores = new ArrayList<>();
        this.id = db.get("email", this.email).get("id");
    }

    //checks if the account exists
    public boolean accountExists() {
        return this.hasAccount;
    }

    //This method allows the user, if customer, to view the list of stores
    //if the customer is not blocked, they can view the list of stores
    public void viewStores() {
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", Role.Seller.toString());
        if (this.role.toLowerCase().equals(Role.Customer.toString().toLowerCase())) {
            System.out.println("Stores:");
            for (HashMap<String, String> sellerBlocked : getSellers) {
                ArrayList<String> stores = User.readStoresFromFile(sellerBlocked.get("email"));
                String storeString = "";
                for (String store : stores) {
                    storeString += store + ", ";
                }
                storeString = storeString.substring(0, storeString.length() - 2);
                if (!sellerBlocked.get("blocked").contains(id) && !db.get("id", id).get("blocked").contains(sellerBlocked.get("id")) && !db.get("id", id).get("invisible").contains(sellerBlocked.get("id"))) {
                    System.out.println(sellerBlocked.get("email") + ": " + storeString);
                } else if (!db.get("id", id).get("blocked").contains(sellerBlocked.get("id")) && !sellerBlocked.get("invisible").contains(id)){
                    System.out.println(sellerBlocked.get("email") + " (blocked): " + storeString);
                } else if (db.get("id", id).get("blocked").contains(sellerBlocked.get("id")) && !sellerBlocked.get("invisible").contains(id)) {
                    System.out.println(sellerBlocked.get("email") + " (invisible): " + storeString);
                }
            }
        }
    }

    //Implementing Seller
    //contains the blocked functionality where the user cannot input the store if the seller is blocked
    public void addStores(String store) throws InvalidUserException {
        if (this.role.toLowerCase().equals("seller")) {
            try {
                if (User.getEmailFromStore(store) != null) {
                    throw new InvalidUserException("That store name is not available");
                }
                File f = new File("stores.txt");
                FileOutputStream fos = new FileOutputStream(f, true);
                PrintWriter pw = new PrintWriter(fos);
                pw.write(store + "-" + email + "\n");
                stores.add(store);
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Seller views list of customers that they can send messages to
    public void viewCustomers() {
        ArrayList<HashMap<String, String>> getCustomers = db.getSelection("role", Role.Customer.toString());
        if (this.role.toLowerCase().equals(Role.Seller.toString().toLowerCase())) {
            System.out.println("Customers:");
            HashMap<String, String> thisUser = db.get("id", id);
            for (HashMap<String, String> sellerBlocked : getCustomers) {
                if (!sellerBlocked.get("invisible").contains(id) && !thisUser.get("blocked").contains(sellerBlocked.get("id")) && !thisUser.get("invisible").contains(sellerBlocked.get("id"))) {
                    System.out.println(sellerBlocked.get("email"));
                } else if (!thisUser.get("blocked").contains(sellerBlocked.get("id")) && !sellerBlocked.get("invisible").contains(id)){
                    System.out.println(sellerBlocked.get("email") + " (invisible)");
                } else if (thisUser.get("blocked").contains(sellerBlocked.get("id")) && !sellerBlocked.get("invisible").contains(id)) {
                    System.out.println(sellerBlocked.get("email") + " (blocked)");
                }
            }
        }
    }

    //get list of stores
    public ArrayList<String> getStores() {
        return stores;
    }

    public static ArrayList<String> readStoresFromFile(String email) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File("Stores.txt")))) {
            ArrayList<String> things = new ArrayList<String>();
            String line;
            while ((line = bfr.readLine()) != null) {
                if (line.contains(email)) {
                    things.add(line.split("-")[0]);
                }
            }
            return things;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String getEmailFromStore(String store) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File("Stores.txt")))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                if (line.contains(store)) {
                    return line.split("-")[1];
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
