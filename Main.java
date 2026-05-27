import StudentMain.Student;

class Student {

    private String name;
    private int id;
    private int age;
    private double gpa;

    // Question 2
    public Student() {
        name = "Unknown";
        id = 0;
        age = 0;
        gpa = 0.0;
    }

    // Question 3:
    public Student(String name) {
        this.name = name;
        id = 0;
        age = 0;
        gpa = 0.0;
    }

    // Question 3
    public Student(String name, int id, int age, double gpa) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.gpa = gpa;
    }

    // Question 4
    public void printStudentInfo() {
        System.out.println("Name: " + name);
        System.out.println("ID: " + id);
        System.out.println("Age: " + age);
        System.out.println("GPA: " + gpa);
        System.out.println();
    }
}

public class Main {
    public static void main(String[] args) {

        // Question 5

        Student student1 = new Student();

        Student student2 = new Student("Alex");
        Student student3 = new Student("Jordan", 12345, 18, 3.7);

        student1.printStudentInfo();
        student2.printStudentInfo();
        student3.printStudentInfo();
    }
}
