import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sumOfEvens = 0;

        System.out.print("Enter 5 integers: ");
        int count = 0;
        while (count < 5) {
            if (scanner.hasNextInt()) {
                int currentNumber = scanner.nextInt();
                int remainder = currentNumber % 2;
                boolean isEven = (remainder == 0);
                if (isEven) {
                    int newSum = sumOfEvens + currentNumber;
                    sumOfEvens = newSum;
                }
                count = count + 1;
            } else {
                String badInput = scanner.next();
                badInput.length();
            }
        }

        System.out.println("Sum of even numbers: " + sumOfEvens);
        scanner.close();
    }
}