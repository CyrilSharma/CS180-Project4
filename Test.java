/**
 * Project 4 -> Test
 *
 * Testing class
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class Test {
    public static void main(String[] args) {
        Database db = new Database("TestDatabase.txt");
        try {
            db.add("Cyril@bohemia.com", "password", Role.Customer);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Dashboard dashboard = new Dashboard("Alex@gmail.com", "testMessageHistory.txt", db);
        dashboard.readDatabase();
        dashboard.printMyStatistic();
        //dashboard.presentDashboard();

        //Sample test for filter
        //Filter f = new Filter("Jacob@gmail.com");
        //        System.out.println(f.toString());
        //        String line = "Freak, I hate you bro, you are such idiOt and DUMB, 
        //            I will never buy anything from this freaking store!";
        //        System.out.println(f.filter(line));
        //        try {
        //            f.add("never");
        //            f.add("idiot");
        //            f.remove("dumb");
        //            System.out.println("------------");
        //            System.out.println(f.toString());
        //            System.out.println(f.filter(line));
        //            f.remove("never");
        //            f.remove("idiot");
        //            f.add("dumb");
        //        } catch (InvalidWordException e) {
        //            e.printStackTrace();
        //        }
        //*

    }
}
