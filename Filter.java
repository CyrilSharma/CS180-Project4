import java.io.*;
import java.util.*;
/**
 * Project 4 -> Filter
 *
 * Allows user to filter out certain words in their conversations and replace with auto-generated asterisks or
 * user-defined phrase(s)
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class Filter {
    private File f;
    private HashMap<String, String> map;
    private ArrayList<String[]> userWordList;
    private Database db;

    private boolean on = false;

    /**
     * Initializes instance fields and creates a file if not already created
     *
     * @param email email of the user
     */
    public Filter(String email, Database db) {
        map = db.get("email", email);
        f = new File(PathManager.storeDir + "UserFilter.txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
            }
        }
        userWordList = new ArrayList<>();
        this.db = db;
        read();
    }

    /**
     * read UserFilter.txt and bring the content into the list
     */
    public void read() {
        String splitter = "::";
        FileReader fr;
        BufferedReader bfr;
        try {
            fr = new FileReader(f);
            bfr = new BufferedReader(fr);
            while (true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                String[] users = line.split(splitter);
                userWordList.add(users);
            }
        } catch (Exception e) {
        }
    }

    /**
     * get list of filtered words
     * @return result, arraylist of filtered words
     */
    public ArrayList<String> get() {
        read();
        String[] words = new String[0];
        for (String[] userData: userWordList) {
            if (userData[0].equals(map.get("id"))) {
                if (userData.length == 1) {
                    words = new String[1];
                    words[0] = "No Word";
                } else {
                    words = userData[1].split(";");
                }
            }
        }
        ArrayList<String> result = new ArrayList<>(Arrays.asList(words));
        return result;
    }

    public HashMap<String, Boolean> getStatuses() {
        String path = "store/FilterStatus.txt";
        MessageManager mm = new MessageManager(db);
        HashMap<String, Boolean> map = new HashMap<>();
        try {
            String[] contents = mm.readTextFromFile(path);
            for (String line: contents) {
                if (line.indexOf("::") == -1) {
                    continue;
                }
                String[] data = line.split("::");
                if (data[1].equals("true")) {
                    map.put(data[0], true);
                } else {
                    map.put(data[0], false);
                }
            }
            return map;
        } catch (IOException e) {

        }
        return new HashMap<>();
    }
    public boolean getStatus(String id) {
        for (String status: getStatuses().keySet()) {
            String[] filterStatus = status.split("::");
            if (filterStatus[0].equals(id)) {
                if (filterStatus[1].equals("true")) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    public synchronized void setFilterStatus(boolean status, String id) {
        String path = "store/FilterStatus.txt";
        File f = new File(path);
        FileOutputStream fos;
        PrintWriter pw;
        try {
            fos = new FileOutputStream(f, false);
            pw = new PrintWriter(fos);
            HashMap<String, Boolean> map = getStatuses();
            map.put(id, status);
            for (String user: map.keySet()) {
                String line = user + "::" + map.get(user);
                pw.println(line);
            }
            pw.close();
        } catch (Exception e) {
        }
    }

    /**
     * remove special characters from word
     * @param word
     * @return
     */
    public String removeSpecialChar(String word) {
        String result = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) > 64 && word.charAt(i) <= 122) {
                result = result + word.charAt(i);
            }
        }
        return result;
    }

    /**
     * returns whether status is on or off
     * @return on
     */
    public boolean getStatus() {
        return on;
    }

    /**
     * checks the line and if the line has the filtered word, replace it with * and return the modified line
     * @param line
     * @return line, modified
     */
    public String filter(String line) {
        ArrayList<String> list = get();
        for (String word : list) {
            line = line.replaceAll(word, createFilteredWord(word.length()));
        }
        return line;
    }

    /**
     * get str composed of * based on length
     * @param size (int)
     * @return word, filtered & replaced w/ *'s
     */
    public String createFilteredWord(int size) {
        String word = "";
        for (int i = 0; i < size; i++) {
            word += "*";
        }
        return word;
    }

    /**
     * turn filtered list into format in txt file
     * @param words (arraylist of strings)
     * @return str
     */
    public String toTxtFileFormat(ArrayList<String> words) {
        String str = "";
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).toLowerCase().equals("no word") || words.get(i).equals("") || words.get(i).equals(" ")) {
                continue;
            }
            str += words.get(i);
            if (i != words.size() - 1) {
                str += ";";
            }
        }
        return str;
    }

    /**
     * remove word from filter and txt file
     * @param word (string)
     */
    public void remove(String word) throws InvalidWordException {
        ArrayList<String> words = get();
        if (!words.contains(word)) {
            throw new InvalidWordException("The word does not exist in the current filter!");
        }
        words.remove(word);
        String id = map.get("id");
        String newLine = toTxtFileFormat(words);
        int index = 0;
        for (String[] user: userWordList) {
            if (user[0].equals(id)) {
                user[1] = newLine;
                userWordList.set(index, user);
            }
            index++;
        }
        write();
    }

    /**
     * add word to the filter and txt file
     * @param word
     */
    public void add(String word) throws InvalidWordException {
        ArrayList<String> words = get();
        if (words.contains(word)) {
            throw new InvalidWordException("The word already exists in the filter");
        }
        words.add(word);
        String id = map.get("id");
        String newLine = toTxtFileFormat(words);
        int index = 0;
        for (String[] user: userWordList) {
            if (user.length == 1) {
                String[] temp = new String[2];
                temp[0] = user[0];
                user = temp;
            }
            if (user[0].equals(id)) {
                user[1] = newLine;
                userWordList.set(index, user);
                break;
            }
            index++;

        }
        if (index == userWordList.size()) {
            String[] userdata = new String[2];
            userdata[0] = id;
            userdata[1] = newLine;
            userWordList.add(userdata);
        }
        write();
    }

    /**
     * write file
     */
    public synchronized void write() {
        read();
        FileOutputStream fos;
        PrintWriter pw;
        HashSet<String> written = new HashSet<>();
        try {
            fos = new FileOutputStream(f, false);
            pw = new PrintWriter(fos);
            for (String[] data: userWordList) {
                if (!written.contains(data[0])) {
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(data[1]));
                    String line = data[0] + "::" + toTxtFileFormat(list);
                    pw.println(line);
                    written.add(data[0]);
                }


            }
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * toString format
     */
    public String toString() {
        String format = "words: ";
        ArrayList<String> list = get();
        if (list.size() == 0) {
            format += "no words";
        } else {
            int index = 0;
            for (String word: list) {
                format += word;
                if (index != list.size() - 1) {
                    format +=  ", ";
                } else {
                    format += "\n";
                }
                index++;
            }
        }

        return format;
    }
}
