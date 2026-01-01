import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");
        int sum = 0;

        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int d = scanner.nextInt();
        int e = scanner.nextInt();

        if (a % 2 == 0) {
            sum += a;
        }
        if (b % 2 == 0) {
            sum += b;
        }
        if (c % 2 == 0) {
            sum += c;
        }
        if (d % 2 == 0) {
            sum += d;
        }
        if (e % 2 == 0) {
            sum += e;
        }

        System.out.println("Sum of even numbers: " + sum);
        scanner.close();
    }
}