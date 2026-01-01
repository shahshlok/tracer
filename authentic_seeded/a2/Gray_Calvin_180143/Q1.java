import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");

        int sum = 0;
        int count = 0;

        while (count < 5) {
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                int remainder = value % 2;
                if (remainder == 0) {
                    int newSum = sum + value;
                    sum = newSum;
                }
                int newCount = count + 1;
                count = newCount;
            } else {
                String invalidInput = scanner.next();
                invalidInput = invalidInput;
            }
        }

        System.out.println("Sum of even numbers: " + sum);
        scanner.close();
    }
}