import java.util.*;
public class Q2 {
    public static void main(String[] args) {
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
                int u = n + 1;
                n = u;
                if (y != x) {
                    if (y > x) {
                        System.out.println("Too high!");
                    } else {
                        if (y < x) {
                            System.out.println("Too low!");
                        }
                    }
                }
            } else {
                String t = s.next();
                String u = t;
            }
        }
        if (n != 0) {
            System.out.println("Correct! You took " + n + " guesses.");
        }
    }
}