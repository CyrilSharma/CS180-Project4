import java.io.*;
import java.util.*;
import java.time.*;

/*
 * TODO: ADD INVISIBILITY FOR USERS AGAINST OTHER USERS
 */

public class MessageManager {

    private ArrayList<HashMap<String, String>> database;

    // Commas are not allowed to be used for usernames or passwords
    // in fact, no special characters are allowed
    // it makes splitting the database complicated
    private final String DATABASE_SPLIT = ";;;";
    private final String BLOCKED_SPLIT_STRING = ",";
    private Random random;

    //Initializes the database and random
    public MessageManager() {
        database = readFromDatabase();
        random = new Random();
    }

    //Creates a new user and for that to happen, the following parameters are needed
    //blocked arraylist contains all the ids of people who are blocked by this customer (usernames can be changed, IDs can't) (can be null)
    //blocking arraylist contains all the ids of people who are blocking this customer
    public void add(String name, String password, Role role, ArrayList<String> blocked) throws InvalidUserException {
        if (get(name) != null) {
            throw new InvalidUserException("That username is already taken");
        }
        if (!validate(name)) {
            throw new InvalidUserException("Your username cannot include special characters");
        }
        if (!validate(password)) {
            throw new InvalidUserException("Your password cannot include special characters");
        }
        String id = "";
        do {
            for (int i = 0; i < 14; i++) {
                int num = random.nextInt(62);
                if (num < 26) {
                    id += (char)(num + 'A');
                } else if (num < 52) {
                    id += (char)(num - 26 + 'a');
                } else {
                    id += (char)(num - 52 + '0');
                }
            }
        } while (get(getUsername(id)) != null);
        String line = createFormattedString(id, name, password, role.toString(), Instant.now().toString(), blockArrayListToString(blocked));
        database.add(getDatabaseEntry(line));
        writeToDatabase();
    }

    //Allows you to remove a user from the database, TODO: Sync with removal with the deletion of message history
    public void remove(String name) throws InvalidUserException {
        HashMap<String, String> toBeRemoved = get(name);
        if (toBeRemoved == null) {
            throw new InvalidUserException("That username does not exist");
        }
        if (database.remove(toBeRemoved)) {
            writeToDatabase();
        }
    }

    //Allows you to edit your username, must enter current username
    public void setName(String name, String newName) throws InvalidUserException {
        HashMap<String, String> changeInfo = get(name);
        if (changeInfo == null) {
            throw new InvalidUserException("That username does not exist");
        }
        if (get(newName) != null) {
            throw new InvalidUserException("That username is already taken");
        }
        if (!validate(newName)) {
            throw new InvalidUserException("Your username cannot include special characters");
        }
        changeInfo.put("username", newName);
        writeToDatabase();
    }

    //Allows a user to edit their password but must provide their name, old password, and new password
    public void setPassword(String name, String oldPassword, String newPassword) throws InvalidUserException {
        if (verify(name, oldPassword)) {
            HashMap<String, String> changeInfo = get(name);
            changeInfo.put("password", newPassword);
            writeToDatabase();
        } else {
            throw new InvalidUserException("The password you entered is not correct");
        }
    }

    //Sets the time someone was last online to the present moment
    public void setLastOnline(String name) throws InvalidUserException {
        HashMap<String, String> changeInfo = get(name);
        if (changeInfo == null) {
            throw new InvalidUserException("That username does not exist");
        }
        changeInfo.put("lastOnline", Instant.now().toString());
        writeToDatabase();
    }

    //Adds someone to the blocked list in the database
    public void addBlocked(String name, String usernameToBlock) throws InvalidUserException {
        HashMap<String, String> changeInfo = get(name);
        if (changeInfo == null) {
            throw new InvalidUserException("That username does not exist");
        }
        HashMap<String, String> blockedUser = get(usernameToBlock);
        if (blockedUser == null) {
            throw new InvalidUserException("The user you want to block does not exist");
        }
        ArrayList<String> blockedUsers = getBlocked(name);
        if (blockedUsers.size() == 1 && blockedUsers.get(0).equals("null")) {
            blockedUsers = new ArrayList<String>();
        }
        if (blockedUsers.contains(getID(usernameToBlock))) {
            throw new InvalidUserException("You already blocked that user");
        }
        blockedUsers.add(getID(usernameToBlock));
        changeInfo.put("blocked", blockArrayListToString(blockedUsers));
        writeToDatabase();
    }

    //Removes someone from the blocked list
    public void removeBlocked(String name, String usernameToUnblock) throws InvalidUserException {
        HashMap<String, String> changeInfo = get(name);
        if (changeInfo == null) {
            throw new InvalidUserException("That username does not exist");
        }
        HashMap<String, String> blockedUser = get(usernameToUnblock);
        if (blockedUser == null) {
            throw new InvalidUserException("The user you want to unblock does not exist");
        }
        ArrayList<String> blockedUsers = getBlocked(name);
        if (blockedUsers.remove(getID(usernameToUnblock))) {
            changeInfo.put("blocked", blockArrayListToString(blockedUsers));
            writeToDatabase();
        } else {
            throw new InvalidUserException("You were not blocking that user");
        }
    }

    // Searches through the database and pulls out a user's information, represented in a HashMap
    private HashMap<String, String> get(String name) {
        for (HashMap<String, String> user : database) {
            if (user.get("username").equals(name)) {
                return user;
            }
        }
        return null;
    }

    //Returns a user's username based on their ID
    public String getUsername(String id) {
        for (HashMap<String, String> user : database) {
            if (user.get("id").equals(id)) {
                return user.get("username");
            }
        }
        return null;
    }

    //Definitely unnecessary but it completes the set
    public String getName(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return user.get("username");
    }

    private String getPassword(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return user.get("password");
    }

    //Returns a user's 14-character ID
    public String getID(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return user.get("id");
    }

    //Returns the role a user has (Customer or Seller)
    public Role getRole(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        Role role = Role.valueOf(user.get("role"));
        return role;
    }

    //Gets the instant that someone was last online
    public Instant getLastOnline(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return Instant.parse(user.get("lastOnline"));
    }

    //Gets an ArrayList of people blocked from messaging a user
    public ArrayList<String> getBlocked(String username) {
        HashMap<String, String> user = get(username);
        if (user == null) {
            return null;
        }
        return blockStringToArrayList(user.get("blocked"));
    }

    //Converts a string representation of everyone blocked by a user to an ArrayList
    public ArrayList<String> blockStringToArrayList(String block) {
        if (block == null || block.equals("") || block.equals("null")) {
            return new ArrayList<String>();
        }
        ArrayList<String> blockedList = new ArrayList<String>();
        blockedList.addAll(Arrays.asList(block.split(BLOCKED_SPLIT_STRING)));
        return blockedList;
    }

    //Converts an ArrayList of the various people blocked by a user to a string
    public String blockArrayListToString(ArrayList<String> blockList) {
        String blocked = "";
        if (blockList == null || blockList.isEmpty()) {
            return null;
        }
        for (String item : blockList) {
            blocked += item + BLOCKED_SPLIT_STRING;
        }
        blocked = blocked.substring(0, blocked.lastIndexOf(BLOCKED_SPLIT_STRING));
        return blocked;
    }

    //Returns the names of all the customers a user can talk to
    public ArrayList<String> getCustomerNames(String name) throws InvalidUserException {
        if (get(name) == null) {
            throw new InvalidUserException("That name does not exist");
        }
        if (getRole(name) == Role.Customer) {
            throw new InvalidUserException("Customers cannot see the names of other customers");
        }
        ArrayList<String> customerNames = getUserNames(true);
        if (customerNames.isEmpty()) {
            return null;
        }
        for (int i = customerNames.size() - 1; i >= 0; i--) {
            String names = customerNames.get(i);
            if (getBlocked(names).contains(getID(name))) {
                customerNames.remove(i);
            }
        }
        return customerNames;
    }

    //Gets all the sellers a customer can see
    public ArrayList<String> getSellerNames(String name) throws InvalidUserException {
        if (get(name) == null) {
            throw new InvalidUserException("That name does not exist");
        }
        if (getRole(name) == Role.Seller) {
            throw new InvalidUserException("Sellers cannot see the names of other sellers");
        }
        ArrayList<String> sellerNames = getUserNames(false);
        if (sellerNames.isEmpty()) {
            return null;
        }
        for (int i = sellerNames.size() - 1; i >= 0; i--) {
            String names = sellerNames.get(i);
            if (getBlocked(names).contains(getID(name))) {
                sellerNames.remove(i);
            }
        }
        return sellerNames;
    }

    //Returns everything in the user database into an arraylist of HashMaps
    private ArrayList<HashMap<String, String>> readFromDatabase() {
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File("UserDatabase.txt")))) {
            ArrayList<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
            String line;
            while((line = bfr.readLine()) != null) {
                userList.add(getDatabaseEntry(line));
            }
            if (userList.isEmpty()) {
                return new ArrayList<HashMap<String, String>>();
            } else {
                return userList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Returns everything for one user from the database into a HashMap
    private HashMap<String, String> readFromDatabase(String username) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File("UserDatabase.txt")))) {
            String line;
            while((line = bfr.readLine()) != null) {
                HashMap<String, String> userLine = getDatabaseEntry(line);
                if (userLine.get("username").equals(username)) {
                    return userLine;
                }
            }
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<String> getUserNames(boolean customer) {
        ArrayList<String> userNames = new ArrayList<String>();
        if (database.isEmpty()) {
            return userNames;
        }
        for (HashMap<String, String> user : database) {
            String name = user.get("username");
            if (customer && getRole(name) == Role.Customer) {
                userNames.add(name);
            } else if (!customer && getRole(name) == Role.Seller) {
                userNames.add(name);
            }
        }
        return userNames;
    }

    // Creates a string formatted to the specifications of being added to the database
    private String createFormattedString(String id, String username, String password, String role, String lastOnline, String blocked) {
        return id + DATABASE_SPLIT + username + DATABASE_SPLIT + password + DATABASE_SPLIT + role + DATABASE_SPLIT + lastOnline + DATABASE_SPLIT + blocked;
    }

    //Checks to make sure a string that will go to the database has NO SPECIAL CHARACTERS
    private boolean validate(String str) {
        return str.matches("^[A-Za-z0-9]+$");
    }

    // Returns whether the password given for a user was the correct password
    public boolean verify(String name, String password) throws InvalidUserException {
        String databasePassword = getPassword(name);
        if (databasePassword == null) {
            throw new InvalidUserException("That username does not exist");
        }
        return databasePassword.equals(password);
    }

    // Returns a HashMap object by splitting the string from the database
    private HashMap<String, String> getDatabaseEntry(String userString) {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] lineArray = userString.split(DATABASE_SPLIT);
        map.put("id", lineArray[0]);
        map.put("username", lineArray[1]);
        map.put("password", lineArray[2]);
        map.put("role", lineArray[3]);
        map.put("lastOnline", lineArray[4]);
        map.put("blocked", lineArray[5]);
        return map;
    }

    // Commits everything in the database ArrayList to the file UserDatabase.txt
    private void writeToDatabase() {
        try {
            File file = new File("UserDatabase.txt");
            file.createNewFile();
            PrintWriter pw = new PrintWriter(file);
            String string = "";
            if (database != null && !database.isEmpty()){
                string = toString();
            }
            pw.write(string.strip());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        String string = "";
        for (HashMap<String, String> userInfo : database) {
            string += createFormattedString(userInfo.get("id"), userInfo.get("username"), userInfo.get("password"), userInfo.get("role"), userInfo.get("lastOnline"), userInfo.get("blocked")) + "\n";
        }
        return string;
    }

    public void messageUser(String username, String usernameToSendMessageTo, String message) {
        //TODO: Implement messageUser
    }
}