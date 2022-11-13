import java.io.*;
import java.util.*;

public class TestMainInterface2 {
    // Useful constants.
    private static final String dbPath = "UserDatabase.txt";
    private static final String storePath = "Stores.txt";
    private static final String inputPath = "testMainInterface2/inputs.txt";
    private static final String outputPath = "testMainInterface2/outputs.txt";
    private static final String expectedPath = "testMainInterface2/expected.txt";
    private static ArrayList<String> messageIDs = new ArrayList<String>();
    public static void main(String[] args) throws IOException {
        System.out.println("Test " + (test0() ? "Passed" : "Failed"));
        String[] args2 = null;
        if (System.getProperty("os.name").contains("Windows")) {
            args2 = new String[] {"cmd.exe", "/c", "java MainInterface < testMainInterface2/inputs.txt > testMainInterface2/outputs.txt", "with", "args"};
        } else {
            args2 = new String[] {"/bin/bash", "-c", "java MainInterface < testMainInterface2/inputs.txt > testMainInterface2/outputs.txt", "with", "args"};
        }
        Process proc = new ProcessBuilder(args2).start();
    }
    public static boolean test0() {
        // tests construction.
        try {
            PrintWriter pw = new PrintWriter(new File(dbPath));
            pw.write("");
            pw.flush();
            pw.close();
            pw = new PrintWriter(new File(storePath));
            pw.write("");
            pw.flush();
            pw.close();
            boolean passedTest = true;
            BufferedReader bfr = new BufferedReader(new FileReader(new File(outputPath)));
            BufferedReader bfr2 = new BufferedReader(new FileReader(new File(expectedPath)));
            String line;
            String line2;
            while ((line = bfr.readLine()) != null && (line2 = bfr2.readLine()) != null) {
                if (!line.equals(line2)) {
                    System.out.println("Comparison Failure: " + "<" + line.strip() + "> " + "<" + line2.strip() + ">");
                    passedTest = false;
                }
            }
            bfr.close();
            bfr2.close();
            return passedTest;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}