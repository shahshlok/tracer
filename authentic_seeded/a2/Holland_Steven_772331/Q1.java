import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sum = 0;
        int count = 5;

        System.out.print("Enter 5 integers: ");
        for (int i = 0; i < count; i++) {
            int n = scanner.nextInt();
            int a = n % 2;
            if (a == 0) {
                sum += n;
            }
        }

        System.out.println("Sum of even numbers: " + sum);
        scanner.close();
    }
}