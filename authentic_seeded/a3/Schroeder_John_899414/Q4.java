import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter size: ");
        int n = scanner.nextInt();

        int[] arr = new int[n];

        if (n > 0) {
            System.out.print("Enter elements: ");
        }

        for (int i = 0; i < n; i++) {
            int value = scanner.nextInt();
            arr[i] = value;
        }

        if (n > 1) {
            int lastElement = arr[n - 1];
            for (int i = n - 1; i > 0; i--) {
                int temp = arr[i - 1];
                arr[i] = temp;
            }
            arr[0] = lastElement;
        }

        System.out.print("Shifted: ");
        for (int i = 0; i < n; i++) {
            int value = arr[i];
            System.out.print(value);
            if (i != n - 1) {
                System.out.print(" ");
            }
        }
    }
}