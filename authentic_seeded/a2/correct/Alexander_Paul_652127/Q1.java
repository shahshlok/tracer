import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");
        int sum = 0;

        for (int i = 0; i < 5; i++) {
            int value = scanner.nextInt();
            if (value % 2 == 0) {
                sum += value;
            }
        }

        System.out.println("Sum of even numbers: " + sum);
        scanner.close();
    }
}