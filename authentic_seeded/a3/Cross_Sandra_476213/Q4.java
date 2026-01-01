import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter size: ");
        int n = scanner.nextInt();

        int[] numbers;
        if (n > 0) {
            numbers = new int[n];
        } else {
            numbers = new int[0];
        }

        System.out.print("Enter elements: ");
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                int value = scanner.nextInt();
                numbers[i] = value;
            }
        }

        if (n > 1) {
            int lastElement = numbers[n - 1];
            int i = n - 1;
            while (i > 0) {
                int previousValue = numbers[i - 1];
                numbers[i] = previousValue;
                i = i - 1;
            }
            numbers[0] = lastElement;
        }

        System.out.print("Shifted: ");
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                int current = numbers[i];
                System.out.print(current);
                if (i != n - 1) {
                    System.out.print(" ");
                }
            }
        }
    }
}