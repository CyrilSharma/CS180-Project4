import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Project 5 -> User
 *
 * Creates the Customer and Seller distinctions and their unique functions/restrictions
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class User implements Serializable {
    //Instance fields
    private String email;
    private boolean hasAccount;
    private Role role;
    private Database db;
    private ArrayList<String> stores;
    private String id; //user's personal ID

    /**
     * creating an account
     *
     * @param email, password, role, manager
     * @throws Exception
     */
    public User(String email, String password, Role role, MessageManager manager, Database db) throws Exception {
        this.email = email;
        this.role = role;
        this.hasAccount = true;
        this.db = db;
        stores = readStoresFromFile(email);
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

    public String getID() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    /**
     * checks if the account exists
     *
     * @return hasAccount boolean
     * @throws Exception
     */
    public HashMap<String, String> viewStores() throws Exception {
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", Role.Seller.toString());
        HashMap<String, String> stores = new HashMap<>();
        if (role.equals(Role.Customer)) {
            for (HashMap<String, String> sellerBlocked : getSellers) {
                if (!sellerBlocked.get("invisible").contains(id)) {
                    ArrayList<String> stores2 = User.readStoresFromFile(sellerBlocked.get("email"));
                    String email = sellerBlocked.get("email");
                    for (String store : stores2) {
                        stores.put(store, email);
                    }
                    //TODO: fix so that invisible people aren't added to the list
                }
            }
        }
        return stores;
    }

    /**
     * Allows seller to create a store under their name that is visible to all
     * unblocked customers
     * @param store
     * @throws Exception
     */
    public void addStores(String store) throws Exception {
        if (role.equals(Role.Seller)) {
            try {
                if (User.getEmailFromStore(store) != null || store.contains("-") || store.contains(",,,")) {
                    throw new InvalidUserException("That store name is not available");
                }
                File f = new File(PathManager.storeDir + "Stores.txt");
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f, true);
                PrintWriter pw = new PrintWriter(fos);
                pw.write(store + "---" + email + "\n");
                stores.add(store);
                pw.close();
            } catch (IOException e) {
                throw new IOException("An error has occurred creating your store");
            }
        }
    }
    /**
     * Seller can view list of customers that they can send messages to
     */

    //TODO: Test this function, repeatedly
    public ArrayList<String> viewCustomers() {
        ArrayList<HashMap<String, String>> getCustomers = db.getSelection("role", Role.Customer.toString());
        ArrayList<String> customers = new ArrayList<>();
        if (role.equals(Role.Seller)) {
            for (HashMap<String, String> sellerBlocked : getCustomers) {
                String userEmail = sellerBlocked.get("email");
                if (!sellerBlocked.get("invisible").contains(id)) {
                    customers.add(userEmail);
                }
            }
        }
        return customers;
    }

    /**
     * get list of stores
     * @return stores
     */
    public HashMap<String, String> getStores() {
        HashMap<String, String> result = new HashMap<>();
        for (String store : stores) {
            result.put(store, email);
        }
        return result;
    }

    /**
     * Read text file with list of stores and return as an arraylist of string (stores)
     *
     * @param email
     * @return things as arraylist of strings with stores from the file
     * @throws Exception
     */
    public static ArrayList<String> readStoresFromFile(String email) throws Exception {
        try (BufferedReader bfr = new BufferedReader(new FileReader(
                new File(PathManager.storeDir + "Stores.txt")))) {
            ArrayList<String> things = new ArrayList<String>();
            String line;
            while ((line = bfr.readLine()) != null) {
                if (line.contains(email)) {
                    things.add(line.split("---")[0]);
                }
            }
            return things;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Gets seller name from the associated store
     */
    public static String getEmailFromStore(String store) throws Exception {
        try (BufferedReader bfr = new BufferedReader(new FileReader(
                new File(PathManager.storeDir + "Stores.txt")))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                if (line.contains(store)) {
                    return line.split("---")[1];
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return null;
    }

    public ArrayList<String> getUsers() throws Exception {
        ArrayList<String> userList = new ArrayList<>();
        if (role.equals(Role.Customer)) {
            HashMap<String, String> users = viewStores();
            userList.addAll(users.values());
            for (int i = 0; i < userList.size(); i++) {
                userList.set(i, userList.get(i).split(" ")[0]);
            }
        } else {
            userList = viewCustomers();
        }
        return userList;
    }
}
