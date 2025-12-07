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
                int temp = value;
                if (temp % 2 == 0) {
                    sum = sum + temp;
                }
                count = count + 1;
            } else {
                String badInput = scanner.next();
            }
        }

        System.out.println("Sum of even numbers: " + sum);

        scanner.close();
    }
}