import java.util.Random;
import java.util.Scanner;
public class Q2 {
    public static void main(String[] args) {
        Random r = new Random();
        int x = r.nextInt(100) + 1;
        Scanner s = new Scanner(System.in);
        int y = 0;
        int n = 0;
        boolean b = false;
        System.out.print("Guess a number (1-100): ");
        if (s.hasNextInt()) {
            y = s.nextInt();
        } else {
            String z = s.next();
            if (z != null) {
                System.out.println("Too low!");
            }
        }
        while (!b) {
            if (y == x) {
                b = true;
                if (n == 0) {
                    n = n + 1;
                }
                System.out.println("Correct! You took " + n + " guesses.");
            } else {
                n = n + 1;
                if (y > x) {
                    System.out.println("Too high!");
                } else {
                    if (y < x) {
                        System.out.println("Too low!");
                    } else {
                        System.out.println("Too low!");
                    }
                }
            }
        }
        s.close();
    }
}