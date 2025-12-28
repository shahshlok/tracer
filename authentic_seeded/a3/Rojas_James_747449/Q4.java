import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter size: ");
        int n = scanner.nextInt();

        int[] numbers = new int[n];

        System.out.print("Enter elements: ");
        for (int i = 0; i < n; i++) {
            numbers[i] = scanner.nextInt();
        }

        if (n > 1) {
            int lastElement = numbers[n - 1];
            int i = n - 1;
            while (i > 0) {
                numbers[i] = numbers[i - 1];
                i = i - 1;
            }
            numbers[0] = lastElement;
        }

        System.out.print("Shifted: ");
        for (int i = 0; i < n; i++) {
            System.out.print(numbers[i]);
            if (i != n - 1) {
                System.out.print(" ");
            }
        }
    }
}