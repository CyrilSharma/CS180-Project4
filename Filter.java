import java.io.*;
import java.util.*;

public class Filter {
    private File f;
    private Database db;
    private HashMap<String, String> map;
    private ArrayList<String[]> userWordList;

    //using one txt file that has all users' blocked/filtered words
    public Filter(String email) {
        db = new Database("UserDatabase.txt");
        map = db.get("email", email);
        f = new File("UserFilter.txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                System.out.println("Error occured while trying to create a file!");
                e.printStackTrace();
            }
        }
        userWordList = new ArrayList<>();
        readFile();
    }
    //read UserFilter.txt and bring the content into the list
    public void readFile() {
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
            e.printStackTrace();
            System.out.println("Database Error!");
        }
    }

    //get list of filtered words
    public ArrayList<String> getFilteredWordList() {
        String[] words = new String[0];
        for (String[] userData: userWordList) {
            if (userData[0].equals(map.get("id"))) {
                words = userData[1].split(";");
            }
        }
        ArrayList<String> result = new ArrayList<>(Arrays.asList(words));
        return result;
    }

    //remove special characters from word
    public String removeSpecialChar(String word) {
        String result = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) > 64 && word.charAt(i) <= 122) {
                result = result + word.charAt(i);
            }
        }
        return result;
    }

    //checks the line and if the line has the filtered word, replace it with * and return the modified line
    public String filterWords(String line) {
        //TODO: FIX THIS METHOD
        ArrayList<String> list = getFilteredWordList();
        for (String word: list) {
            int index = line.toLowerCase().indexOf(word.toLowerCase());
            Character c = ' ';
            System.out.println(index);
            System.out.println(index);
            if (index != -1 && (index == 0 || line.charAt(index-1) == 32) &&
                    (index == line.length() - word.length()) || line.charAt(index+word.length()) == 32) {
                line = line.replaceAll(word, createFilteredWord(word.length()));
            }
        }
        return line;
    }

    public String createFilteredWord(int size) {
        String word = "";
        for (int i = 0; i < size; i++) {
            word += "*";
        }
        return word;
    }

    //turn filtered list into format in txt file
    public String toTxtFileFormat(ArrayList<String> words) {
        String str = "";
        for (int i = 0; i < words.size(); i++) {
            str += words.get(i);
            if (i == words.size() - 1) {
                str += ";";
            }
        }
        return str;
    }

    //add word to the filter txt file
    public void addWordToFilter(String word) {
        ArrayList<String> words = getFilteredWordList();
        words.add(word);
        String id = map.get("id");
        String newLine = id + "::" + toTxtFileFormat(words);
        int index = 0;
        for (String[] user: userWordList) {
            if (user[0].equals(id)) {
                user[1] = newLine;
                userWordList.set(index, user);
            }
            index++;
        }
        writeToFile();
    }

    public void writeToFile() {
        FileOutputStream fos;
        PrintWriter pw;
        try {
            fos = new FileOutputStream(f, false);
            pw = new PrintWriter(fos);
            for (String[] data: userWordList) {
                ArrayList list = new ArrayList(Arrays.asList(data[1]));
                String line = data[0] + "::" + toTxtFileFormat(list);
                pw.println(line);
            }
            pw.close();

        } catch (Exception e) {
            System.out.println("Exception occurred!!");
            e.printStackTrace();
        }
    }

    public String toString() {
        String format = "id: %s\nwords: ";
        String.format(format, map.get("id"));
        ArrayList<String> list = getFilteredWordList();
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
        return format;
    }
}
