import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");
        int sum = 0;

        for (int i = 1; i < 5; i++) {
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