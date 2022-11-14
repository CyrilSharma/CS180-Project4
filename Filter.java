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

    private boolean on = false;

    /**
     * Initializes instance fields and creates a file if not already created
     *
     * @param email email of the user
     */
    public Filter(String email) {
        db = new Database("UserDatabase.txt");
        map = db.get("email", email);
        f = new File("UserFilter.txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                System.out.println("Error occured while trying to create a file!");
            }
        }
        userWordList = new ArrayList<>();
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
            while(true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                String[] users = line.split(splitter);
                userWordList.add(users);
            }
        } catch (Exception e) {
            System.out.println("Database Error!");
        }
    }

    /**
     * get list of filtered words
     * @return result, arraylist of filtered words
     */
    public ArrayList<String> get() {
        String[] words = new String[0];
        for (String[] userData: userWordList) {
            if (userData[0].equals(map.get("id"))) {
                if (userData.length == 1) {
                    words = new String[1];
                    words[0] = "no word";
                } else {
                    words = userData[1].split(";");
                }
            }
        }
        ArrayList<String> result = new ArrayList<>(Arrays.asList(words));
        return result;
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
            if (words.get(i).equals("no word") || words.get(i).equals("") || words.get(i).equals(" ")) {
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
    public void remove(String word) throws InvalidWordException{
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
    public void write() {
        FileOutputStream fos;
        PrintWriter pw;
        try {
            fos = new FileOutputStream(f, false);
            pw = new PrintWriter(fos);
            for (String[] data: userWordList) {
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(data[1]));
                String line = data[0] + "::" + toTxtFileFormat(list);
                pw.println(line);
            }
            pw.close();

        } catch (Exception e) {
            System.out.println("Exception occurred!!");
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

    /**
     * UI for user to utilize Filter class
     */
    public void presentFilterMenu(Scanner sc, boolean filter) {

        String menu;
        boolean ongoing = true;
        while(ongoing) {
            menu = "what do you want to do?\n";
            if (on) {
                menu += "1. disable filter\n";
            } else {
                menu += "1. enable filter\n";
            }
            menu += "2. see filtered words\n" +
                    "3. add word to the filter\n" +
                    "4. remove the word from filter\n" +
                    "5. quit";
            System.out.println(menu);
            String option = sc.nextLine();
            switch (option) {
                case "1":
                    on = !on;
                    break;
                case "2":
                    System.out.println(toString());
                    break;
                case "3":
                    System.out.println("please enter a word to be filtered");
                    String word = sc.nextLine();
                    try {
                        add(word);
                        System.out.println("Successfully added a word!");
                        System.out.println(toString());
                    } catch (Exception e) {
                        System.out.println("Error while trying to add a word");
                    }
                    break;
                case "4":
                    System.out.println("please enter a word to remove from filter");
                    String wor = sc.nextLine();
                    try {
                        remove(wor);
                        System.out.println("Successfully removed a word!");
                        System.out.println(toString());
                    } catch (Exception e) {
                        System.out.println("Error while trying to remove a word");
                    }
                    break;
                case "5":
                    ongoing = false;
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
}
