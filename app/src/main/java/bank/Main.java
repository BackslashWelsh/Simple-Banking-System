package bank;

public class Main {
    public static void main(String[] args) {
        // set database name "-fileName *.s3db"
        if (args.length == 2 && args[0].equals("-fileName")
                && args[1].contains(".s3db"))
            new Bank(args[1]);
        else
            new Bank();
    }
}