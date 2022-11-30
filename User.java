import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Project 4 -> User
 *
 * Creates the Customer and Seller distinctions and their unique functions/restrictions
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class User {
    //Instance fields
    private String email;
    private boolean hasAccount;
    private String role;
    private Database db;
    private ArrayList<String> stores;
    private String id; //user's personal ID

    /**
     * creating an account
     *
     * @param email, password, role, manager
     */
    public User(String email, String password, String role, MessageManager manager, Database db) {
        this.email = email;
        this.role = role;
        this.hasAccount = true;
        this.db = db;
        stores = new ArrayList<>();
        this.id = db.get("email", this.email).get("id");
    }

    /**
     * checks if the account exists
     *
     * @return hasAccount boolean
     */
    public boolean accountExists() {
        return this.hasAccount;
    }

    /**
     * checks if the account exists
     *
     * @return hasAccount boolean
     */
    public HashMap<String, String> viewStores() {
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", Role.Seller.toString());
        HashMap<String, String> stores = new HashMap<>();
        if (this.role.toLowerCase().equals(Role.Customer.toString().toLowerCase())) {
            System.out.println("Stores:");
            for (HashMap<String, String> sellerBlocked : getSellers) {
                if (!sellerBlocked.get("blocked").contains(id) && !sellerBlocked.get("invisible").contains(id)) {
                    ArrayList<String> stores2 = User.readStoresFromFile(sellerBlocked.get("email"));
                    String id = sellerBlocked.get("id");
                    if (!sellerBlocked.get("blocked").contains(id) 
                        && !db.get("id", id).get("blocked").contains(sellerBlocked.get("id")) 
                        && !db.get("id", id).get("invisible").contains(sellerBlocked.get("id"))) {
                        id = sellerBlocked.get("id");
                    } if (!db.get("id", id).get("blocked").contains(sellerBlocked.get("id")) 
                        && !sellerBlocked.get("invisible").contains(id)) {
                        id += " (blocked)";
                    } else if (db.get("id", id).get("blocked").contains(sellerBlocked.get("id")) 
                        && !sellerBlocked.get("invisible").contains(id)) {
                        id += " (invisible)";
                    }
                    for (String store : stores2) {
                        stores.put(store, id);
                    }
                }
            }
        }
        return stores;
    }

    /**
     * Allows seller to create a store under their name that is visible to all
     * unblocked customers
     * @param store
     */
    public void addStores(String store) throws InvalidUserException {
        if (this.role.toLowerCase().equals("seller")) {
            try {
                if (User.getEmailFromStore(store) != null || store.contains("-")) {
                    throw new InvalidUserException("That store name is not available");
                }
                File f = new File(PathManager.storeDir + "Stores.txt");
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f, true);
                PrintWriter pw = new PrintWriter(fos);
                pw.write(store + "-" + email + "\n");
                stores.add(store);
                pw.close();
            } catch (IOException e) {
                System.out.println("An error has occurred creating your store");
            }
        }
    }
    /**
     * Seller can view list of customers that they can send messages to
     */
    public void viewCustomers() {
        ArrayList<HashMap<String, String>> getCustomers = db.getSelection("role", Role.Customer.toString());
        HashMap<String, String> customers;
        if (this.role.toLowerCase().equals(Role.Seller.toString().toLowerCase())) {
            System.out.println("Customers:");
            HashMap<String, String> thisUser = db.get("id", id);
            for (HashMap<String, String> sellerBlocked : getCustomers) {
                if (!sellerBlocked.get("invisible").contains(id) 
                    && !thisUser.get("blocked").contains(sellerBlocked.get("id")) 
                    && !thisUser.get("invisible").contains(sellerBlocked.get("id"))) {
                    System.out.println(sellerBlocked.get("email"));
                } else if (!thisUser.get("blocked").contains(sellerBlocked.get("id")) 
                    && !sellerBlocked.get("invisible").contains(id)) {
                    System.out.println(sellerBlocked.get("email") + " (invisible)");
                } else if (thisUser.get("blocked").contains(sellerBlocked.get("id")) 
                    && !sellerBlocked.get("invisible").contains(id)) {
                    System.out.println(sellerBlocked.get("email") + " (blocked)");
                }
            }
        }
    }

    /**
     * get list of stores
     * @return stores
     */
    public ArrayList<String> getStores() {
        return stores;
    }

    /**
     * Read text file with list of stores and return as an arraylist of string (stores)
     *
     * @param email
     * @return things as arraylist of strings with stores from the file
     */
    public static ArrayList<String> readStoresFromFile(String email) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(
                new File(PathManager.storeDir + "Stores.txt")))) {
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

    /**
     * Gets seller name from the associated store
     */
    public static String getEmailFromStore(String store) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(
                new File(PathManager.storeDir + "Stores.txt")))) {
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
