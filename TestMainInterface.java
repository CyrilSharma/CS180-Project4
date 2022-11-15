import java.io.File;
import java.io.IOException;

/**
 * Project 4 -> TestMainInterface
 *
 * class handles the testing of main interface
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class TestMainInterface {
    public static void main(String[] args) throws IOException {
        
        File dir = new File("testMainInterface/inputs");
        File[] files = dir.listFiles();
        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = files[i].getName();
        }

        for (String name: filenames) {
            String[] commandArgs = null;
            if (System.getProperty("os.name").equals("Windows 10")) {
                commandArgs = new String[] {"cmd.exe", "/c", String.format("java MainInterface" 
                + " testMainInterface/ < testMainInterface/inputs/%s" 
                + " > testMainInterface/outputs/%s", name, name), "with", "args"};
            } else {
                commandArgs = new String[] {"/bin/bash", "-c", String.format("java MainInterface" 
                + " testMainInterface/ < testMainInterface/inputs/%s" 
                + " > testMainInterface/outputs/%s", name, name), "with", "args"};
            }
            Process proc = new ProcessBuilder(commandArgs).start();
            try {
                proc.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Clean Up Environment.
            File userFilter = new File("testMainInterface/UserFilter.txt");
            File stores = new File("testMainInterface/Stores.txt");
            File history = new File("testMainInterface/history");
            File csv = new File("testMainInterface/csv");
            File db = new File("testMainInterface/Database.txt");
            for (File f: history.listFiles()) {
                f.delete();
            }
            for (File f: csv.listFiles()) {
                f.delete();
            }
            userFilter.delete();
            stores.delete();
            db.delete();
        }
    }
}