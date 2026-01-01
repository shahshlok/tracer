import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sum = 0;

        System.out.print("Enter 5 integers: ");
        for (int i = 0; i < 5; i++) {
            int num = scanner.nextInt();
            if (num % 2 == 0) {
                sum += num;
            }
        }

        System.out.println("Sum of even numbers: " + sum);
        scanner.close();
    }
}