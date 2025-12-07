import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");

        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int d = scanner.nextInt();
        int e = scanner.nextInt();

        int sum = 0;

        int x = a % 2;
        if (x == 0) {
            sum += a;
        }

        x = b % 2;
        if (x == 0) {
            sum += b;
        }

        x = c % 2;
        if (x == 0) {
            sum += c;
        }

        x = d % 2;
        if (x == 0) {
            sum += d;
        }

        x = e % 2;
        if (x == 0) {
            sum += e;
        }

        System.out.println("Sum of even numbers: " + sum);

        scanner.close();
    }
}