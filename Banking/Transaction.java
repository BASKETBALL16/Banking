package Banking;

public class Transaction {

    private String date;
    private String type;
    private double amount;
    private String description;

    public Transaction(String date, String type, double amount, String description) {
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String toString() {
        return date + " | " + type + " | $" +
                String.format("%.2f", amount) + " | " + description;
    }
}