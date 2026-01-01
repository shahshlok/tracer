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
        int i = 0;
        while (i < n) {
            arr[i] = scanner.nextInt();
            i = i + 1;
        }

        System.out.print("Enter target: ");
        int target = scanner.nextInt();

        int indexFound = -1;
        int j = 0;
        while (j < n && indexFound == -1) {
            int current = arr[j];
            if (current == target) {
                indexFound = j;
            }
            j = j + 1;
        }

        if (indexFound != -1) {
            System.out.println("Found at index: " + indexFound);
        } else {
            System.out.println(-1);
        }

        scanner.close();
    }
}