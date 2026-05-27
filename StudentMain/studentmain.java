package StudentMain;
public class studentmain {

        public static void main(String[] args) {
    
            Student s1 = new Student("Alice", 3.6);
            Student s2 = new Student("Bob");
    
            System.out.println(s1.getName() + " GPA: " + s1.getGpa());
            System.out.println(s2.getName() + " GPA: " + s2.getGpa());
    
            s2.setGpa(500);
    
            System.out.println(s2.getName() + " GPA after invalid update: " + s2.getGpa());
        }
    }
    