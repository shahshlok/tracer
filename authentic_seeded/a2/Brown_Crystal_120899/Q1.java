import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sum = 0;

        System.out.print("Enter 5 integers: ");
        for (int i = 1; i < 5; i++) {
            int number = scanner.nextInt();
            if (number % 2 == 0) {
                sum += number;
            }
        }

        System.out.println("Sum of even numbers: " + sum);
        scanner.close();
    }
}