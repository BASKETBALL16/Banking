public class Human {

    public void startConvo() {
        System.out.println("Can you give me a name? I forgot mine...");
    }

    public void doMentalMath(int a, int b) {
        System.out.println(a + " plus " + b + " is " + (a + b) + ".");
    }
}
class Worker extends Human {

    @Override
    public void startConvo() {
        System.out.println("I'm a waitress.");
    }
}

class Mathematician extends Human {

    @Override
    public void doMentalMath(int a, int b) {
        System.out.println(
            "A rectangle of length " + a +
            " and height " + b +
            " is " + (a * b) + " units squared."
        );
    }
}

class Driver {

    public static void main(String[] args) {

        Human h1 = new Worker();
        h1.startConvo();
        h1.doMentalMath(2, 3);

        System.out.println();

        Human h = new Mathematician();
        h.startConvo();
        h.doMentalMath(2, 3);
    }
}
