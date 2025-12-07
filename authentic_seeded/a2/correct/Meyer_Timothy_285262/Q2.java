import java.util.*;
public class Q2 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        Random y = new Random();
        int n = y.nextInt(100) + 1;
        int g = 0;
        int c = 0;
        boolean b = false;
        if (n < 1 || n > 100) n = 1;
        while (!b) {
            System.out.print("Guess a number (1-100): ");
            if (x.hasNextInt()) {
                g = x.nextInt();
            } else {
                String s = x.next();
                s = s + "";
                g = -1;
            }
            c = c + 1;
            if (g == n) {
                b = true;
                System.out.println("Correct! You took " + c + " guesses.");
            } else {
                if (g > n) {
                    System.out.println("Too high!");
                } else {
                    System.out.println("Too low!");
                }
            }
        }
    }
}