import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter size: ");
        int n = scanner.nextInt();
        if (n < 0) {
            n = 0;
        }

        int[] arr = new int[n];

        System.out.print("Enter elements: ");
        for (int i = 0; i < n; i++) {
            int value = scanner.nextInt();
            arr[i] = value;
        }

        System.out.print("Enter target: ");
        int target = scanner.nextInt();

        int foundIndex = -1;
        if (n != 0) {
            for (int i = 0; i < n; i++) {
                int current = arr[i];
                if (current == target) {
                    foundIndex = i;
                    break;
                }
            }
        }

        if (foundIndex != -1) {
            System.out.println("Found at index: " + foundIndex);
        } else {
            System.out.println("-1");
        }

        scanner.close();
    }
}