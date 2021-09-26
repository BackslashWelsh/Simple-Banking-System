package bank;

public class Main {
    public static void main(String[] args) {
        if (args.length == 2 && args[0].equals("-fileName")
                && args[1].contains(".s3db"))
            new Bank(args[1]);
        else
            new Bank();
    }
}
    // or maybe take a rest
// figure out with Luhn// if user doesn't paas Luhn dont search & print massege
// look at people's works & refactor
// add default to switch