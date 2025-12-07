import java.util.*;
public class Q2 {
    public static void main(String[] a) {
        Random r = new Random();
        int n = r.nextInt(100) + 1;
        Scanner s = new Scanner(System.in);
        int x = 0;
        int y = 0;
        while (x != n) {
            System.out.print("Guess a number (1-100): ");
            if (s.hasNextInt()) {
                int t = s.nextInt();
                x = t;
                int u = y + 1;
                y = u;
                if (x == n) {
                    int v = y;
                    System.out.println("Correct! You took " + v + " guesses.");
                } else {
                    if (x > n) {
                        System.out.println("Too high!");
                    } else {
                        if (x < n) {
                            System.out.println("Too low!");
                        }
                    }
                }
            } else {
                String z = s.next();
                String w = z;
                if (w != null) {
                    System.out.println("Please enter an integer.");
                }
            }
        }
    }
}