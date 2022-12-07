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
}
