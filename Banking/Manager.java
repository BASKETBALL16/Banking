package Banking;

import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Manager {
    private ArrayList<Account> accounts = new ArrayList<>();
    private int nextID = 1;

    private static final String DATABASE_FILE = "bank_database.txt";

    public boolean createAccount(User user, String accountType, double initialDeposit) {
        accountType = accountType.toUpperCase();

        if (!accountType.equals("SAVINGS")) {
            for (Account acc : accounts) {
                if (isSameUser(acc.getUser(), user) && acc.getAccountType().equals(accountType)) {
                    System.out.println("User already has this account type.");
                    return false;
                }
            }
        }

        Account newAccount;

        if (accountType.equals("CHECKING")) {
            newAccount = new Account.CheckingAccount(user, initialDeposit, nextID++);
        } else if (accountType.equals("SAVINGS")) {
            newAccount = new Account.SavingsAccount(user, initialDeposit, nextID++);
        } else if (accountType.equals("ROTHIRA")) {
            newAccount = new Account.RothIRA(user, initialDeposit, nextID++);
        } else {
            System.out.println("Unknown account type: " + accountType);
            return false;
        }

        accounts.add(newAccount);
        System.out.println("Account created: " + newAccount.getAccountID());

        saveToDatabase(newAccount);

        return true;
    }

    // Appends one new account to the file when it's first created
    private void saveToDatabase(Account acc) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(DATABASE_FILE, true));
            writer.println(
                acc.getAccountType() + "," +
                acc.getUser().getFirstName() + "," +
                acc.getUser().getLastName() + "," +
                acc.getUser().getPin() + "," +
                acc.getBalance()
            );
            writer.close();
            System.out.println("Saved to database: " + DATABASE_FILE);
        } catch (IOException e) {
            System.out.println("Error saving to database: " + e.getMessage());
        }
    }

    // Overwrites the entire file with current balances — call this on logout
    public void saveDatabase() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(DATABASE_FILE, false));
            for (Account acc : accounts) {
                writer.println(
                    acc.getAccountType() + "," +
                    acc.getUser().getFirstName() + "," +
                    acc.getUser().getLastName() + "," +
                    acc.getUser().getPin() + "," +
                    acc.getBalance()
                );
            }
            writer.close();
            System.out.println("Database saved.");
        } catch (IOException e) {
            System.out.println("Error saving database: " + e.getMessage());
        }
    }

    public void loadFromDatabase() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 5) continue;

                String accountType = parts[0];
                String firstName   = parts[1];
                String lastName    = parts[2];
                String pin         = parts[3];
                double balance     = Double.parseDouble(parts[4]);

                User user = new User(firstName, lastName, pin);
                Account loadedAccount;

                if (accountType.equals("CHECKING")) {
                    loadedAccount = new Account.CheckingAccount(user, balance, nextID++);
                } else if (accountType.equals("SAVINGS")) {
                    loadedAccount = new Account.SavingsAccount(user, balance, nextID++);
                } else if (accountType.equals("ROTHIRA")) {
                    loadedAccount = new Account.RothIRA(user, balance, nextID++);
                } else {
                    System.out.println("Unknown account type in file: " + accountType);
                    continue;
                }

                accounts.add(loadedAccount);
                System.out.println("Loaded from database: " + loadedAccount.getAccountID());
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("No existing database found. Starting fresh.");
        }
    }

    public void printAllAccounts() {
        for (Account acc : accounts) {
            System.out.println(acc);
        }
    }

    public Account getAccount(User user, String accountType) {
        accountType = accountType.toUpperCase();
        for (Account acc : accounts) {
            if (isSameUser(acc.getUser(), user) && acc.getAccountType().equals(accountType)) {
                return acc;
            }
        }
        return null;
    }

    private boolean isSameUser(User a, User b) {
        return a.getFirstName().equals(b.getFirstName()) &&
               a.getLastName().equals(b.getLastName()) &&
               a.getPin().equals(b.getPin());
    }
}