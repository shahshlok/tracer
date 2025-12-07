import java.util.Scanner;

public class Q1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");

        int count = 0;
        int sumOfEvens = 0;

        while (count < 5) {
            if (scanner.hasNextInt()) {
                int currentValue = scanner.nextInt();
                int remainder = currentValue % 2;
                if (remainder == 0) {
                    int newSum = sumOfEvens + currentValue;
                    sumOfEvens = newSum;
                }
                int newCount = count + 1;
                count = newCount;
            } else {
                String badInput = scanner.next();
            }
        }

        System.out.println("Sum of even numbers: " + sumOfEvens);

        scanner.close();
    }
}