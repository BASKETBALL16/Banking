package StudentMain;
public class Student {

    private String name;
    private double gpa;

    public Student(String name, double gpa) {
        this.name = name;
        setGpa(gpa);
    }

    public Student(String name) {
        this.name = name;
        this.gpa = 4.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        if (gpa >= 0.0 && gpa <= 4.0) {
            this.gpa = gpa;
        } else {
            System.out.println("Error: GPA must be between 0 and 4");
        }
    }
}
