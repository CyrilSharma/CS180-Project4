import java.util.ArrayList;
import java.util.HashMap;
/**
 * Project 5 -> DashboardInterfaceGUI
 *
 * Dashboardinterface that connects with the Dashboard GUI
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class DashboardInterfaceGUI {
    private Translator tr;
    public DashboardInterfaceGUI() {
        tr = new Translator();
    }

    /**
     * returns the storeMap which is distinguished by the role of seller or customer
     *
     * @param role, email
     * @return HashMap<String, String> of the storeMap</String,>
     */
    public HashMap<String, String> getStoreMap(Role role, String email) {
        HashMap<String, String> map = new HashMap<>();
        try {
            if (role == Role.Customer) {
                map = (HashMap<String, String>) tr.query(new Query("Dashboard", "getStoreMap"));
            } else {
                String[] param = new String[1];
                param[0] = email;
                map = (HashMap<String, String>) tr.query(new Query("Dashboard", "getMyStoreMap", param));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * returns the customer statistics
     *
     * @param id
     * @return HashMap<String, String> of the customerStats</String,>
     */
    public HashMap<String, HashMap<String, ArrayList<Object>>> customerStats(String id) {
        try {
            String[] param = {id};
            HashMap<String, HashMap<String, ArrayList<Object>>> map =
                    (HashMap<String, HashMap<String, ArrayList<Object>>>)
                            tr.query(new Query("Dashboard", "getCustomerStats",param));
            return map;
        } catch (Exception e) {

        }
        return null;
    }
    /**
     * returns the seller statistics
     *
     * @param id
     * @return HashMap<String, String> of the sellerStats</String,>
     */
    public HashMap<String, HashMap<String, ArrayList<Object>>> sellerStats(String id) {
        try {
            String[] param = {id};
            HashMap<String, HashMap<String, ArrayList<Object>>> map =
                    (HashMap<String, HashMap<String, ArrayList<Object>>>)
                            tr.query(new Query("Dashboard", "getSellerStats",param));
            return map;
        } catch (Exception e) {

        }
        return null;
    }
    /**
     * returns the id
     *
     * @param email
     * @return String id
     */
    public String getID(String email) {
        HashMap<String, String> map;
        String id = "";
        try {
            map = tr.get("email", email);
            id = map.get("id");
        } catch (Exception e) {
        }
        return id;
    }
    /**
     * returns the email
     *
     * @param ID
     * @return String email
     */
    public String getEmail(String ID) {
        HashMap<String, String> map;
        String email = "";
        try {
            map = tr.get("id", ID);
            email = map.get("email");
        } catch (Exception e) {
        }
        return email;
    }
}
