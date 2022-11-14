import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class TestMainInterface {
    // Useful constants.
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

            // wait for process to finish.
            try {
                proc.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            File input = new File(String.format("testMainInterface/outputs/%s", name));
            File expected = new File(String.format("testMainInterface/expected/%s", name));
            boolean success = Files.mismatch(input.toPath(), expected.toPath()) == -1;
            System.out.printf("Test %s %s\n", name.substring(0, name.length() - 4),
                success ? "passed." : "failed.");

            // Cleanup
            File db = new File("testMainInterface/databases/db.txt");
            db.delete();
            File history = new File("testMainInterface/history");
            for (File f: history.listFiles()) {
                f.delete();
            }
        }
    }
}