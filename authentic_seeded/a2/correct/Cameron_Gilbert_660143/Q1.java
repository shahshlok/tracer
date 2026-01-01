import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");

        int count = 0;
        int sum = 0;

        while (count < 5) {
            int value = scanner.nextInt();
            int tempValue = value;

            if (tempValue % 2 == 0) {
                sum = sum + tempValue;
            }

            count = count + 1;
        }

        int finalSum = sum;
        System.out.println("Sum of even numbers: " + finalSum);

        scanner.close();
    }
}