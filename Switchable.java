/*create an interface Switchable and method turnOn()
create a parent class for Appliance
create class Toaster that is an Appliance and Switchable
overload turnOn() in Toaster so it mimics a timer using int */


public interface Switchable {
    void turnOn();
}

class Appliance {
    private String brand;

    public String getBrand() {
        return brand;
    }
}

class Toaster extends Appliance implements Switchable {

    public void turnOn() {
        System.out.println("Toaster is on.");
    }

    public void turnOn(int seconds) {
        System.out.println("Toaster is on for " + seconds + " seconds.");
    }
}
