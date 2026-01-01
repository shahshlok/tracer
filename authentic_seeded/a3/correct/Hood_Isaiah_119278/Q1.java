import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter size: ");
        int n = sc.nextInt();

        int[] arr = new int[n];

        System.out.print("Enter elements: ");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        System.out.print("Enter target: ");
        int t = sc.nextInt();

        int index = -1;
        int i = 0;
        while (i < n && index == -1) {
            if (arr[i] == t) {
                index = i;
            }
            i++;
        }

        if (index == -1) {
            System.out.println("-1");
        } else {
            System.out.println("Found at index: " + index);
        }

        sc.close();
    }
}