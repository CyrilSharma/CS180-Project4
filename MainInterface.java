import java.util.Scanner;

public class MainInterface {

    public static void main(String[] args) {
        String PROMPT = "Would you like to..." +
        "\n1. Login" +
        "\n2. Create an account" + 
        "\n3. Exit\n";

        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the {**NAME**} application");
        System.out.println(PROMPT);
        int resp = -1;
        do {
            try {
                resp = scan.nextInt();
                scan.nextLine();
                if (resp < 1 || resp > 3) {
                    System.out.println("Input invalid, try again.");
                    System.out.println(PROMPT);
                    resp = -1;
                }
            } catch (Exception e) {
                System.out.println("Input invalid, try again.");
                System.out.println(PROMPT);
            }
        } while (resp == -1);

        MessageManager messageManager = new MessageManager();
        if (resp == 1) {
            System.out.println("Please enter your username.");
            String username = scan.nextLine();
            System.out.println("Please enter your password.");
            String password = scan.nextLine();
        } else if (resp == 2) {

        } else if (resp == 3) {
            System.out.println("Bye bye!");
        }
    }
}
