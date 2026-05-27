package Banking;

public class User {
    private String firstName;
    private String lastName;
    private String pin;

    public User(String firstName, String lastName, String pin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pin = pin;
    }

    public boolean verifyPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public void setPin(String newPin) {
        this.pin = newPin;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPin() { return pin; }
}