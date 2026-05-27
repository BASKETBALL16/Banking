//Next, let's implement a cow subclass together. Recall that to have a subclass inherit from another,
// all it needs is the keyword extends in the class declaration. 
// After that, we'll need to fill in the specific attributes and methods of a cow!


class Animal {
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void makeSound() {
        System.out.println("The animal makes a sound.");
    }
}

class Cow extends Animal {
    public Cow(String name, int age){
       super(name, age);
    }
    public void makeSound() {
        System.out.println("Moo!");
    }
}

class Main {
    public static void main(String[] args) {
        Cow cow = new Cow("john", 5);

        System.out.println("Name: " + cow.name);
        System.out.println("Age: " + cow.age);
        cow.makeSound();
    }
}


