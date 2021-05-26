

public class main {

    public static void main(String args[]) {

        RicardAgrawala  R = new RicardAgrawala(15);
        R.addRequest(4);
        R.addRequest(0);
        R.addRequest(1);
        R.addRequest(2);
        R.addRequest(14);

        R.printAllRequests();
        R.printAllTimestamps();
        R.printStatus();
        System.out.println("\n");
        R.Start();
        System.out.println("\n");


    }
}
