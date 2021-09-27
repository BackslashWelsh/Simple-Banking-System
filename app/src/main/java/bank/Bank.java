package bank;

import java.util.Scanner;

public class Bank {
    private final DataBase database;
    private final Scanner input = new Scanner(System.in);

    private boolean isExit;
    private int dataBaseSize;
    private int clientID;

    Bank() {
        database = new DataBase("accounts.s3db");
        this.dataBaseSize = database.getID();
        welcomeMenu();
    }

    Bank(String dataBaseName) {
        database = new DataBase(dataBaseName);
        this.dataBaseSize = database.getID();
        welcomeMenu();
    }

    private void welcomeMenu() {
        while (!isExit) {
            System.out.print("\n1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit\n" +
                    "> ");
            choices(input.nextInt());
        }
    }

    private void choices(int choice) {
        switch (choice) {
            case 1:
                createAccount();
                break;
            case 2:
                login();
                break;
            case 0:
                isExit = true;
                break;
            default:
                System.out.println("There is no such a choice.");
                break;
        }
    }

    private void accountMenu() {
        while (!isExit) {
            System.out.print("\n1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit\n" +
                    "> ");
            accountChoices(input.nextInt());
        }
    }

    private void accountChoices(int choice) {
        switch (choice) {
            case 1:
                getBalance();
                break;
            case 2:
                addBalance();
                break;
            case 3:
                transfer();
                break;
            case 4:
                System.out.println(
                        database.deleteAccount(clientID));
            case 5:
                welcomeMenu();
                break;
            case 0:
                isExit = true;
                clientID = 0;
                break;
            default:
                System.out.println("There is no such a choice.");
                break;
        }
    }

    private void getBalance() {
        int balance = database.getBalance(clientID);
        if (balance == -1) {
            System.out.println("Sorry something went wrong try again.");
        } else {
            System.out.println("Balance: " + balance);
        }
    }

    private void createAccount() {
        database.createAccount(new Account(++dataBaseSize));
    }

    public void login() {
        while (!isExit) {
            System.out.print("Enter your card number:\n> ");
            int cardID = getCardID();

            System.out.print("Enter your PIN:\n> ");
            boolean idToPIN = checkCardPIN(cardID);

            if (cardID == -1 || !idToPIN) {
                System.out.println("\nWrong card number or PIN!");
                return;
            } else {
                System.out.println("\nYou have successfully logged in!");
                clientID = cardID;
                accountMenu();
            }
        }
    }

    private int getCardID() {
        String cardN = input.next();

        if (checkByLuhn(cardN)) {
           return database.getCardID(cardN);
        } else {
            System.out.println("Probably you made a mistake in the card number. " +
                    "Please try again!");
            return -1;
        }
    }

    private boolean checkByLuhn(String cardNumber) {
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 == 0) {
                int doubleDigit = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
                sum += doubleDigit;
                continue;
            }
            sum += digit;
        }
        return sum % 10 == 0;
    }

    private boolean checkCardPIN(int cardID) {
        return database.isIdToPIN(cardID, input.next());
    }

    private void addBalance() {
        System.out.println("Enter income:");
        System.out.println(database.addBalance(clientID, input.nextInt()));
    }

    private void transfer() {
        System.out.println("Transfer\n" +
                "Enter card number:");
        int transferID = getTransferID();

        if (transferID == -1)
            return;

        System.out.println("Enter how much money you want to transfer:");
        int amount = input.nextInt();

        if (amount > database.getBalance(clientID))
            System.out.println("Not enough money!");
        else
            database.transfer(clientID, transferID, amount);
    }

    private int getTransferID() {
        int transferID = -1;
        String cardN = input.next();

        if (!checkByLuhn(cardN))
            System.out.println("Probably you made a mistake in the card number." +
                    " Please try again!");
        else if ((transferID = database.getCardID(cardN)) == -1)
            System.out.println("Such a card does not exist.");
        else if (transferID == clientID) {
            System.out.println("You can't transfer money to the same account!");
            transferID = -1;
        }
        return transferID;
    }
}
