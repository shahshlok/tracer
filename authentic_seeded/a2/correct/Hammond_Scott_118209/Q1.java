import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");
        int sum = 0;

        int count = 0;
        while (count < 5) {
            int value = scanner.nextInt();
            int modulus = value % 2;
            if (modulus == 0) {
                int newSum = sum + value;
                sum = newSum;
            }
            count = count + 1;
        }

        System.out.println("Sum of even numbers: " + sum);

        scanner.close();
    }
}