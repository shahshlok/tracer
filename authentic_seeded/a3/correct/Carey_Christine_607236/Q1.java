import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter size: ");
        int n = scanner.nextInt();
        int size = n;

        if (size < 0) {
            size = 0;
        }

        int[] arr = new int[size];

        System.out.print("Enter elements: ");
        for (int i = 0; i < size; i++) {
            int value = scanner.nextInt();
            arr[i] = value;
        }

        System.out.print("Enter target: ");
        int t = scanner.nextInt();
        int target = t;

        int foundIndex = -1;
        int i = 0;

        while (i < size && foundIndex == -1) {
            int current = arr[i];
            if (current == target) {
                foundIndex = i;
            }
            i = i + 1;
        }

        if (foundIndex != -1) {
            System.out.println("Found at index: " + foundIndex);
        } else {
            System.out.println("-1");
        }

        scanner.close();
    }
}