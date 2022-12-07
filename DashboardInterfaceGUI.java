import java.util.HashMap;

public class DashboardInterfaceGUI {
    private Translator tr;
    public DashboardInterfaceGUI() {
        tr = new Translator();
    }

    public HashMap<String, String> getStoreMap(Role role) {
        HashMap<String, String> map = new HashMap<>();
        try {
            if (role == Role.Customer) {
                map = (HashMap<String, String>) tr.query(new Query("Dashboard", "getStoreMap"));
            } else {
                map = (HashMap<String, String>) tr.query(new Query("Dashboard", "getMyStoreMap"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return map;
    }
}
