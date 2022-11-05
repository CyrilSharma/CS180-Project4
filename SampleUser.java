import java.util.*;
//sample user class
public class SampleUser {
    private String username;
    private ArrayList<String> stores;
    public SampleUser(String username, ArrayList<String> stores) {
        this.username = username;
        this.stores = stores;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getStores() {
        return stores;
    }


}


