import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 5;
        int sum = 0;

        System.out.print("Enter 5 integers: ");

        for (int i = 0; i < count; i++) {
            int number = scanner.nextInt();
            int remainder = number % 2;
            if (remainder == 0) {
                int tempSum = sum + number;
                sum = tempSum;
            }
        }

        System.out.println("Sum of even numbers: " + sum);
        scanner.close();
    }
}