import java.util.*;
public class Q2 {
    public static void main(String[] a) {
        Random r = new Random();
        int x = r.nextInt(100) + 1;
        int y = 0;
        int n = 0;
        Scanner s = new Scanner(System.in);
        while (y != x) {
            System.out.print("Guess a number (1-100): ");
            if (s.hasNextInt()) {
                int t = s.nextInt();
                y = t;
                int u = y;
                if (u < 1 || u > 100) {
                    System.out.println("Too low!");
                } else {
                    int v = n;
                    n = v + 1;
                    if (u == x) {
                        System.out.println("Correct! You took " + n + " guesses.");
                    } else {
                        if (u > x) {
                            System.out.println("Too high!");
                        } else {
                            if (u < x) {
                                System.out.println("Too low!");
                            }
                        }
                    }
                }
            } else {
                String z = s.next();
            }
        }
    }
}