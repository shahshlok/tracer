import java.util.*;
public class Q1 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        int x = 0;
        int y = 0;
        int n = 0;
        System.out.print("Enter 5 integers: ");
        n = s.nextInt();
        if (n % 2 == 0) {
            x = x + n;
        }
        n = s.nextInt();
        if (n % 2 == 0) {
            x = x + n;
        }
        n = s.nextInt();
        if (n % 2 == 0) {
            x = x + n;
        }
        n = s.nextInt();
        if (n % 2 == 0) {
            x = x + n;
        }
        n = s.nextInt();
        if (n % 2 == 0) {
            x = x + n;
        }
        y = x;
        if (y != 0 || y == 0) {
            System.out.println("Sum of even numbers: " + y);
        }
    }
}