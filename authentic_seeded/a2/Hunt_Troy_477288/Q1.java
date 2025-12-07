import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int a;
        int b;
        int c;
        int d;
        int e;

        int sumEven = 0;

        System.out.print("Enter 5 integers: ");
        a = scanner.nextInt();
        b = scanner.nextInt();
        c = scanner.nextInt();
        d = scanner.nextInt();
        e = scanner.nextInt();

        int n1 = a;
        int n2 = b;
        int n3 = c;
        int n4 = d;
        int n5 = e;

        if (n1 % 2 == 0) {
            sumEven += n1;
        }
        if (n2 % 2 == 0) {
            sumEven += n2;
        }
        if (n3 % 2 == 0) {
            sumEven += n3;
        }
        if (n4 % 2 == 0) {
            sumEven += n4;
        }
        if (n5 % 2 == 0) {
            sumEven += n5;
        }

        System.out.println("Sum of even numbers: " + sumEven);

        scanner.close();
    }
}