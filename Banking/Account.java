package Banking;

import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Account {
    private User user;
    private String accountType;
    private double balance;
    private String accountID;

    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Account(User user, String accountType, double initialDeposit, int idNumber) {
        this.user = user;
        this.accountType = accountType.toUpperCase();
        this.balance = Math.max(0, initialDeposit);
        this.accountID = generateID(idNumber, user.getFirstName(), user.getLastName());
    }

    private String generateID(int idNum, String firstName, String lastName) {
        int asciiSum = 0;
        String fullName = firstName + lastName;
        for (char c : fullName.toCharArray()) {
            asciiSum += (int) c;
        }
        return accountType.charAt(0) + "-" + (asciiSum + idNum);
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add(new Transaction("Today", "DEPOSIT", amount, "Deposit to " + accountID));
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;

            transactions.add(new Transaction("Today", "WITHDRAWAL", amount, "Withdrawal from " + accountID));
            return true;
        }
        System.out.println("Insufficient funds.");
        return false;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public void printStatement() {
        System.out.println("=== Transaction History for " + accountID + " ===");
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
    }

    public void exportStatementToFile() {
        String fileName = accountID + ".txt";

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            writer.println("============================");
            writer.println("       BANK STATEMENT       ");
            writer.println("============================");
            writer.println("Account Holder: " + user.getFirstName() + " " + user.getLastName());
            writer.println("Account ID:     " + accountID);
            writer.println("Account Type:   " + accountType);
            writer.println("============================");

            if (transactions.isEmpty()) {
                writer.println("No transactions on record.");
            } else {
                for (Transaction t : transactions) {
                    writer.println(t);
                }
            }

            writer.println("============================");
            writer.println("Account Balance: $" + String.format("%.2f", balance));
            writer.println("============================");

            writer.close(); 
            System.out.println("Statement saved to: " + fileName);

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public User getUser() { return user; }
    public String getAccountID() { return accountID; }

    @Override
    public String toString() {
        return user.getFirstName() + " " + user.getLastName() +
               " | " + accountType +
               " | Balance: $" + String.format("%.2f", balance) +
               " | ID: " + accountID;
    }


    public static class CheckingAccount extends Account {

        private double overdraftLimit;

        public CheckingAccount(User user, double initialDeposit, int idNumber) {
            super(user, "CHECKING", initialDeposit, idNumber);
            this.overdraftLimit = 500.00;
        }

        @Override
        public boolean withdraw(double amount) {
            if (amount <= 0) return false;

            double newBalance = getBalance() - amount;

            if (newBalance >= -overdraftLimit) {
                setBalance(newBalance);

                if (newBalance < 0) {
                    setBalance(getBalance() - 35.00);
                    System.out.println("Warning: You are overdrawn! A $35.00 overdraft fee has been charged.");
                    System.out.println("New balance: $" + String.format("%.2f", getBalance()));
                }

                return true;
            }

            System.out.println("Withdrawal denied. Exceeds overdraft limit of $" + overdraftLimit);
            return false;
        }

        public boolean internalTransfer(double amount, Account destination) {
            if (amount <= 0) return false;

            if (getBalance() >= amount) {
                setBalance(getBalance() - amount);
                destination.deposit(amount);

                System.out.println("Transferred $" + String.format("%.2f", amount) +
                        " to account " + destination.getAccountID());
                return true;
            } else {
                System.out.println("failed to make transfer, insufficient funds");
                return false;
            }
        }

        public double getOverdraftLimit() { return overdraftLimit; }
        public void setOverdraftLimit(double limit) { this.overdraftLimit = limit; }
    }

    public static class SavingsAccount extends Account {

        private double interestRate;

        public SavingsAccount(User user, double initialDeposit, int idNumber) {
            super(user, "SAVINGS", initialDeposit, idNumber);
            this.interestRate = 0.01;
        }

        public void applyMonthlyInterest() {
            double interest = getBalance() * (interestRate / 12);
            setBalance(getBalance() + interest);

            System.out.printf("Interest applied: +$%.2f | New balance: $%.2f%n",
                    interest, getBalance());
        }

        public double getInterestRate() { return interestRate; }
        public void setInterestRate(double rate) { this.interestRate = rate; }
    }

    public static class RothIRA extends Account {

        private static final double ANNUAL_LIMIT = 7500.00;
        private double contributionsThisYear;

        public RothIRA(User user, double initialDeposit, int idNumber) {
            super(user, "ROTHIRA", initialDeposit, idNumber);
            this.contributionsThisYear = Math.max(0, initialDeposit);
        }

        @Override
        public boolean deposit(double amount) {
            if (amount <= 0) return false;

            double remaining = ANNUAL_LIMIT - contributionsThisYear;

            if (amount > remaining) {
                System.out.println("Deposit rejected! You can only contribute $" +
                        String.format("%.2f", remaining) + " more this year.");
                return false;
            }

            contributionsThisYear += amount;
            setBalance(getBalance() + amount);

            System.out.printf("Roth IRA deposit: +$%.2f | Total this year: $%.2f / $%.2f%n",
                    amount, contributionsThisYear, ANNUAL_LIMIT);

            return true;
        }

        public void resetYearlyContributions() {
            contributionsThisYear = 0;
            System.out.println("New year — contribution limit reset.");
        }

        public double getRemainingContribution() {
            return ANNUAL_LIMIT - contributionsThisYear;
        }
    }

    public class AutoTransfer {

        public static final String WEEKLY   = "WEEKLY";
        public static final String BIWEEKLY = "BIWEEKLY";
        public static final String MONTHLY  = "MONTHLY";

        private CheckingAccount source;
        private Account destination;
        private double amount;
        private String frequency;
        private boolean active;

        public AutoTransfer(CheckingAccount source, Account destination, double amount, String frequency) {

            if (!frequency.equals(WEEKLY) &&
                !frequency.equals(BIWEEKLY) &&
                !frequency.equals(MONTHLY)) {
                throw new IllegalArgumentException("Frequency must be WEEKLY, BIWEEKLY, or MONTHLY");
            }

            this.source = source;
            this.destination = destination;
            this.amount = amount;
            this.frequency = frequency;
            this.active = true;

            System.out.printf("Auto-transfer created: $%.2f %s | %s → %s%n",
                    amount, frequency, source.getAccountID(), destination.getAccountID());
        }

        public void execute() {
            if (!active) {
                System.out.println("Auto-transfer cancelled. No money moved.");
                return;
            }
            source.internalTransfer(amount, destination);
        }

        public boolean updateAmount(double newAmount) {
            if (newAmount <= 0) {
                System.out.println("Invalid amount.");
                return false;
            }
            double old = amount;
            amount = newAmount;
            System.out.printf("Transfer amount updated: $%.2f → $%.2f%n", old, newAmount);
            return true;
        }

        public boolean updateFrequency(String newFrequency) {
            if (!newFrequency.equals(WEEKLY) &&
                !newFrequency.equals(BIWEEKLY) &&
                !newFrequency.equals(MONTHLY)) {
                System.out.println("Invalid frequency.");
                return false;
            }
            String old = frequency;
            frequency = newFrequency;
            System.out.println("Transfer frequency updated: " + old + " → " + frequency);
            return true;
        }

        public void cancel() {
            if (!active) {
                System.out.println("Auto-transfer already cancelled.");
                return;
            }
            active = false;
            System.out.println("Auto-transfer cancelled.");
        }

        public boolean isActive() { return active; }
        public double getAmount() { return amount; }
        public String getFrequency() { return frequency; }

        @Override
        public String toString() {
            return String.format(
                    "AutoTransfer | $%.2f %s | %s → %s | %s",
                    amount, frequency,
                    source.getAccountID(), destination.getAccountID(),
                    active ? "ACTIVE" : "CANCELLED"
            );
        }
    }
}