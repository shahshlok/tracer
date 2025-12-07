import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");
        int lastSum = 0;

        for (int i = 0; i < 5; i++) {
            int value = scanner.nextInt();
            int sum = 0;
            if (value % 2 == 0) {
                sum += value;
            }
            lastSum = sum;
        }

        System.out.println("Sum of even numbers: " + lastSum);
        scanner.close();
    }
}