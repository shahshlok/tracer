import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter size: ");
        int n = scanner.nextInt();

        int[] arr = new int[n];

        System.out.print("Enter elements: ");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        System.out.print("Enter target: ");
        int t = scanner.nextInt();

        int index = -1;
        int i = 0;

        while (i < n && index == -1) {
            int a = arr[i];
            if (a == t) {
                index = i;
            } else {
                i = i + 1;
            }
        }

        System.out.println("Found at index: " + index);
    }
}