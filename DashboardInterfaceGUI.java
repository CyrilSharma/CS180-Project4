import java.util.ArrayList;
import java.util.HashMap;

public class DashboardInterfaceGUI {
    private Translator tr;
    public DashboardInterfaceGUI() {
        tr = new Translator();
    }

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
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return map;
    }

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

    public String getID(String email) {
        HashMap<String, String> map;
        String id = "";
        try {
            map = tr.get("email", email);
            id = map.get("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getEmail(String ID) {
        HashMap<String, String> map;
        String email = "";
        try {
            map = tr.get("id", ID);
            email = map.get("email");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }
}
