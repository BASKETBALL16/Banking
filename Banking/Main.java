package Banking;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.loadFromDatabase();

        // Launch the GUI and pass the shared manager so it sees all loaded accounts
        new bankGUI(manager);
    }
}