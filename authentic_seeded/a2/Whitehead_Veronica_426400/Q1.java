import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n1, n2, n3, n4, n5;

        System.out.print("Enter 5 integers: ");
        n1 = scanner.nextInt();
        n2 = scanner.nextInt();
        n3 = scanner.nextInt();
        n4 = scanner.nextInt();
        n5 = scanner.nextInt();

        int lastSum = 0;

        int a = n1 % 2;
        if (a == 0) {
            int sum = 0;
            sum += n1;
            lastSum = sum;
        }

        int b = n2 % 2;
        if (b == 0) {
            int sum = 0;
            sum += n2;
            lastSum = sum;
        }

        int c = n3 % 2;
        if (c == 0) {
            int sum = 0;
            sum += n3;
            lastSum = sum;
        }

        int d = n4 % 2;
        if (d == 0) {
            int sum = 0;
            sum += n4;
            lastSum = sum;
        }

        int e = n5 % 2;
        if (e == 0) {
            int sum = 0;
            sum += n5;
            lastSum = sum;
        }

        System.out.println("Sum of even numbers: " + lastSum);

        scanner.close();
    }
}