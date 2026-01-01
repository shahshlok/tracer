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

        if (n > 0) {
            for (int i = 0; i < n - 1; i++) {
                arr[i + 1] = arr[i];
            }
        }

        System.out.print("Shifted: ");
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i]);
            if (i < n - 1) {
                System.out.print(" ");
            }
        }
    }
}