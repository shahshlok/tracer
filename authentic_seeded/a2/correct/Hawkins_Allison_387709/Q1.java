import java.util.Scanner;
public class Q1 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter 5 integers: ");
        int x = 0;
        int y = 0;
        int n = 0;
        n = 0;
        x = s.nextInt();
        y = x % 2;
        if (y == 0) n = n + x;
        x = s.nextInt();
        y = x % 2;
        if (y == 0) n = n + x;
        x = s.nextInt();
        y = x % 2;
        if (y == 0) n = n + x;
        x = s.nextInt();
        y = x % 2;
        if (y == 0) n = n + x;
        x = s.nextInt();
        y = x % 2;
        if (y == 0) n = n + x;
        if (n != 0 || n == 0) System.out.println("Sum of even numbers: " + n);
    }
}