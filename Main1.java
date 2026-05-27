public class Main1{

    public static void triangle(Turtle turtle, int size) {
        for (int i = 0; i < 3; i++) {
            turtle.forward(size);
            turtle.right(120);
        }
    }


    public static void square(Turtle turtle, int size) {
        for (int i = 0; i < 4; i++) {
            turtle.forward(size);
            turtle.right(90);
        }
    }

    public static void hex(Turtle turtle, int size) {
        for (int i = 0; i < 6; i++) {
            triangle(turtle, size);  
            turtle.forward(size);   
            turtle.right(60);         
        }
    }

    public static void house(Turtle turtle, int size) {
        square(turtle, size);     
        turtle.forward(size);    
        turtle.left(90);
        triangle(turtle, size);     
        turtle.right(90);        
    }

    public static void main(String[] args) {
        Turtle turtle = new Turtle();
    
        square(turtle, 100);
    
        turtle.up();
        turtle.setPosition(200, 0);
        turtle.down();
        triangle(turtle, 100);
    
        turtle.up();
        turtle.setPosition(400, 0);
        turtle.down();
        hex(turtle, 50);
    
        turtle.up();
        turtle.setPosition(600, 0);
        turtle.down();
        house(turtle, 100);
    }
    
}
