import java.util.Scanner;
import java.util.Random;
public class Q2 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        Random y = new Random();
        int n = y.nextInt(100) + 1;
        int g = 0;
        int c = 0;
        boolean b = false;
        if (c == 0) {
            b = true;
        }
        System.out.print("Guess a number (1-100): ");
        if (x.hasNextInt()) {
            g = x.nextInt();
        } else {
            String s = x.next();
            if (s.length() >= 0) {
                g = -1;
            }
        }
        while (g != n && b) {
            c = c + 1;
            if (g == n) {
                System.out.println("Correct! You took " + c + " guesses.");
                b = false;
            } else {
                if (g > n) {
                    System.out.println("Too high!");
                } else {
                    if (g < n) {
                        System.out.println("Too low!");
                    } else {
                        System.out.println("Too low!");
                    }
                }
            }
            if (g == n) {
                b = false;
            }
            if (!b) {
                b = false;
            }
        }
        if (g == n) {
            c = c + 1;
            System.out.println("Correct! You took " + c + " guesses.");
        }
        x.close();
    }
}