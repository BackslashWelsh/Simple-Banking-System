package bank;

import java.util.Scanner;

public class Bank {
    private final DataBase dataBase;

    private final Scanner input = new Scanner(System.in);
    private boolean exit;
    private int dataBaseSize;

    private int clientID;

    Bank() {
        dataBase = new DataBase("accounts.s3db");
        this.dataBaseSize = dataBase.getID();
        welcomeMenu();
    }

    Bank(String dbName) {
        dataBase = new DataBase(dbName);
        this.dataBaseSize = dataBase.getID();
        welcomeMenu();
    }

    private void welcomeMenu() {
        while (!exit) {
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
                createNewAcc();
                break;
            case 2:
                login();
                break;
            case 0:
                exit = true;
                break;
            default:
                System.out.println("There is no such a choice.");
                break;
        }
    }

    private void accountMenu() {
        while (!exit) {
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
                int balance = dataBase.getIncome(clientID);
                if (balance == -1)
                    System.out.println("Sorry something went wrong try again.");
                else
                    System.out.println("Balance: " + balance);
                break;
            case 2:
                addIncome();
                break;
            case 3:
                transfer();
                break;
            case 4:
                dataBase.deleteAcc(clientID);
            case 5:
                welcomeMenu();
                break;
            case 0:
                exit = true;
                clientID = 0;
                break;
            default:
                System.out.println("There is no such a choice.");
                break;
        }
    }


    private void createNewAcc() {
//        checkAccSize();
//        accounts[++id] = new Account();
        dataBase.createNewAcc(new Account(++dataBaseSize));
//        dataBase.showDB();
//        clientInfo(id);
    }

    public void login() {
        while (!exit) {
            System.out.print("Enter your card number:\n> ");
            String cardN = input.next();
            if (!checkByLuhn(cardN)) {
                System.out.println("Probably you made a mistake in the card number." +
                        " Please try again!");
                return;
            }
            int cardID = dataBase.getCardID(cardN);

            System.out.print("Enter your PIN:\n> ");
            boolean idToPIN = dataBase.isIdToPIN(cardID, input.next());

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

    private void addIncome() {
        System.out.println("Enter income:");
        System.out.println(dataBase.addIncome(clientID, input.nextInt()));
    }

    private void transfer() {
        int transferID;
        System.out.println("Transfer\n" +
                "Enter card number:");
        String cardN = input.next();
        if (!checkByLuhn(cardN))
            System.out.println("Probably you made a mistake in the card number." +
                    " Please try again!");
        else if ((transferID = dataBase.getCardID(cardN)) == -1)
            System.out.println("Such a card does not exist.");
        else if (transferID == clientID)
            System.out.println("You can't transfer money to the same account!");

        else {
            System.out.println("Enter how much money you want to transfer:");
            int amount = input.nextInt();

            if (amount > dataBase.getIncome(clientID))
                System.out.println("Not enough money!");
            else
                dataBase.transfer(clientID, transferID, amount);
        }
    }


}
