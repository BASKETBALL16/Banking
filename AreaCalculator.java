public class AreaCalculator {

    public static double calculateBudget(int workers, int salary) {
        System.out.println("Original");
        return workers + salary;
    }

    public static double calculateBudget(int workers, int salary, int marketing) {
        System.out.println("With marketing");
        return workers + salary + marketing;
    }

    public static double calculateBudget() {
        System.out.println("No parameters");
        return 0;
    }

    public static void main(String[] args) {
        calculateBudget(5, 1000);
        calculateBudget(5, 1000, 500);
        calculateBudget();
    }
}
