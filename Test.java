public class Test {
    public static void main(String[] args) {
        Database db = new Database("UserDatabase.txt");
        try {
            db.add("Cyril@bohemia.com", "eriuhefur", Role.Customer);
        } catch (InvalidUserException e) {
            System.out.println("Test 1 Failed.");
        }
        try {
            db.add("Joniel@mars.biz", "eriuhefur", Role.Customer);
        } catch (InvalidUserException e) {
            System.out.println("Test 2 Failed.");
        }

        System.out.println(db);
        
        try {
            // NOTE. This will eventually have to be replaced with IDs since names are not unique.
            db.remove("Joniel@mars.biz");
        } catch (InvalidUserException e) {
            System.out.println("Test 3 Failed.");
        }

        System.out.println(db);

        try {
            // NOTE. This will eventually have to be replaced with IDs since names are not unique.
            db.modify("Cyril@bohemia.com", "email", "Qyril@gmail.com");
        } catch (Exception e) {
            System.out.println("Test 3 Failed.");
        }

        // NOTE. This will eventually have to be replaced with IDs since names are not unique.
        if (!db.verify("Qyril@gmail.com", "password")) {
            System.out.println("Test 4 Failed.");
        }

        try {
            // NOTE. This will eventually have to be replaced with IDs since names are not unique.
            db.modify("Cyril", "name", "Qyril");
            System.out.println("Test 5 Failed.");
        } catch (Exception e) {}

        Dashboard dashboard = new Dashboard("Jacob@gmail.com", "testMessageHistory.txt");
        dashboard.readDatabase();
        //dashboard.printConversation();
        dashboard.printMyStatistic();
        //dashboard.presentDashboard();
    }
}
