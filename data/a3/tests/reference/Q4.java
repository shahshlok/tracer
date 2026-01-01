import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter size: ");
        int n = scanner.nextInt();

        int[] arr = new int[n];
        System.out.print("Enter elements: ");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        // Save last element
        int last = arr[n - 1];

        // Shift right
        for (int i = n - 1; i > 0; i--) {
            arr[i] = arr[i - 1];
        }

        // Wrap last to first
        arr[0] = last;

        // Print result
        System.out.print("Shifted: ");
        for (int i = 0; i < n; i++) {
            if (i > 0)
                System.out.print(" ");
            System.out.print(arr[i]);
        }
        System.out.println();
        scanner.close();
    }
}
