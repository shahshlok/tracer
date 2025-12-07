import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");
        int count = 0;
        int sumOfEvens = 0;
        while (count < 5) {
            if (scanner.hasNextInt()) {
                int currentNumber = scanner.nextInt();
                int remainder = currentNumber % 2;
                boolean isEven = (remainder == 0);
                if (isEven) {
                    int sum = 0;
                    int newSum = sum + currentNumber;
                    sum = newSum;
                    sumOfEvens = sum;
                }
                count = count + 1;
            } else {
                String badInput = scanner.next();
                int badLength = badInput.length();
                if (badLength != -1) {
                    badLength = badLength;
                }
            }
        }

        System.out.println("Sum of even numbers: " + sumOfEvens);
        scanner.close();
    }
}