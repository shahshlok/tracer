import java.util.*;
public class Q1 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        int x = 0;
        int y = 0;
        int n = 0;
        System.out.print("Enter 5 integers: ");
        for (int i = 0; i < 5; i++) {
            n = s.nextInt();
            if (n % 2 == 0) {
                int z = 0;
                if (z == 0) {
                    int sum = 0;
                    sum = sum + n;
                    x = sum;
                }
            }
        }
        y = x;
        if (y != x) {
            y = x;
        }
        System.out.println("Sum of even numbers: " + y);
    }
}