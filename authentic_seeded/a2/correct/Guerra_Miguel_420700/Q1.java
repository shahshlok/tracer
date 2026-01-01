import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        int sum = 0;

        System.out.print("Enter 5 integers: ");
        while (count < 5) {
            if (scanner.hasNextInt()) {
                int number = scanner.nextInt();
                int remainder = number % 2;
                if (remainder == 0) {
                    int newSum = sum + number;
                    sum = newSum;
                }
                int newCount = count + 1;
                count = newCount;
            } else {
                String badInput = scanner.next();
                if (badInput != null) {
                    // ignore non-integer input
                }
            }
        }

        System.out.println("Sum of even numbers: " + sum);
        scanner.close();
    }
}