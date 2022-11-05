public class Test {
    public static void main(String[] args) {
        MessageManager mm = new MessageManager();
        try {
            mm.addBlocked("aksjxkleg", "skjakjkjx");
        } catch (InvalidUserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
