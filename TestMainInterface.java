import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class TestMainInterface {
    // Useful constants.
    private static final String dbPath = "testMainInterface/databases/db.txt";
    private static final String histPath = "testMainInterface/history";
    private static final String inputPath = "testMainInterface/inputs";
    private static final String outputPath = "testMainInterface/outputs.txt";
    private static ArrayList<String> messageIDs = new ArrayList<String>();
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
                commandArgs = new String[] {"cmd.exe", "/c", "java MainInterface < testMainInterface/inputs/%s > testMainInterface/outputs/%s", "with", "args"};
            } else {
                commandArgs = new String[] {"/bin/bash", "-c", String.format("java MainInterface < testMainInterface/inputs/%s > testMainInterface/outputs/%s", name, name), "with", "args"};
            }
            Process proc = new ProcessBuilder(commandArgs).start();
            Path inputPath = new File(String.format("testMainInterface/outputs/%s", name)).toPath();
            Path expectedPath = new File(String.format("testMainInterface/expected/%s", name)).toPath();
            boolean success = Files.mismatch(inputPath, expectedPath) == -1;
            System.out.printf("Test %s %s\n", name.substring(0, name.length() - 4),
                success ? "passed." : "failed.");
        }
    }
}