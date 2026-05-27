public class fourone {
    public static int mysterySequence(int n) {
        // base cases
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
    
        // recursive case
        return mysterySequence(n - 1) + mysterySequence(n - 3);
    }
    public static void main(String[] args){
        System.out.println(mysterySequence(7)); // Output: 10
System.out.println(mysterySequence(10)); // Output: 32

    }
}

/*
A.if (y == 1) {
    return x;
}
*/

/*
mystery(x, y - 1)
 */

/*
20
 */


