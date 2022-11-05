import java.util.*;
public class Testing {
    private static final String MENU_MESSAGE = "what do you want to do? \n1. sort stores by alphabetical order \n2. sort stores by alphabetical backwards\n3. sort customers by lowest message sent\n4. sort customers by highest message sent\n5.quit";
    public static void main(String[] args) {
        //Dashboard testing menu
        Scanner sc = new Scanner(System.in);
        Dashboard dashboard = new Dashboard(new SampleCustomer("user1", null));
        dashboard.readAndUpdate();
        dashboard.updateUserStoreData();
        System.out.println("testing customer view");
        dashboard.printMyStatistic(0);
        dashboard.setUser("seller1");
        dashboard.updateUserStoreData();
        System.out.println("testing seller view");
        dashboard.printMyStatistic(0);
        String option;
        while (true) {
            System.out.println(MENU_MESSAGE);
            option = sc.nextLine();
            int opt = 0;
            try {
                opt = Integer.parseInt(option);
            } catch (Exception e) {
                System.out.println("Please Enter a valid number!");
            }
            switch (opt) {
                case 1:
                    dashboard.printMyStatistic(1);
                    break;
                case 2:
                    dashboard.printMyStatistic(2);
                    break;
                case 3:
                    dashboard.printMyStatistic(3);
                    //dashboard.printMyStatistic();
                    break;
                case 4:
                    dashboard.printMyStatistic(4);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Please enter a valid number");
                    break;
            }
        }
        //dashboard.printStatistic();
    }

}
