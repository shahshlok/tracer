import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter size: ");
        int n = scanner.nextInt();

        int[] arr = new int[n];

        System.out.print("Enter elements: ");
        for (int i = 1; i <= n; i++) {
            arr[i - 1] = scanner.nextInt();
        }

        if (n > 0) {
            int a = arr[n];
            for (int i = n; i >= 1; i--) {
                arr[i] = arr[i - 1];
            }
            arr[0] = a;
        }

        System.out.print("Shifted: ");
        for (int i = 1; i <= n; i++) {
            System.out.print(arr[i - 1]);
            if (i < n) {
                System.out.print(" ");
            }
        }
    }
}