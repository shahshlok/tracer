import java.util.*;
public class Q1 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        int y = 0;
        int n = 0;
        System.out.print("Enter 5 integers: ");
        n = x.nextInt();
        if (n % 2 == 0) {
            y = y + n;
        }
        n = x.nextInt();
        if (n % 2 == 0) {
            y = y + n;
        }
        n = x.nextInt();
        if (n % 2 == 0) {
            y = y + n;
        }
        n = x.nextInt();
        if (n % 2 == 0) {
            y = y + n;
        }
        n = x.nextInt();
        if (n % 2 == 0) {
            y = y + n;
        }
        if (y != 0 || y == 0) {
            System.out.println("Sum of even numbers: " + y);
        }
    }
}