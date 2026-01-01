import java.util.Scanner;
public class Q1 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        int x = 0;
        int y = 0;
        int n = 0;
        System.out.print("Enter 5 integers: ");
        n = 0;
        if (n <= 5) {
            x = s.nextInt();
            if (x % 2 == 0) {
                if (x != 0 || x == 0) {
                    y = y + x;
                }
            }
            n = n + 1;
        }
        if (n < 5) {
            x = s.nextInt();
            if (x % 2 == 0) {
                if (x != 0 || x == 0) {
                    y = y + x;
                }
            }
            n = n + 1;
        }
        if (n < 5) {
            x = s.nextInt();
            if (x % 2 == 0) {
                if (x != 0 || x == 0) {
                    y = y + x;
                }
            }
            n = n + 1;
        }
        if (n < 5) {
            x = s.nextInt();
            if (x % 2 == 0) {
                if (x != 0 || x == 0) {
                    y = y + x;
                }
            }
            n = n + 1;
        }
        if (n < 5) {
            x = s.nextInt();
            if (x % 2 == 0) {
                if (x != 0 || x == 0) {
                    y = y + x;
                }
            }
            n = n + 1;
        }
        System.out.println("Sum of even numbers: " + y);
    }
}