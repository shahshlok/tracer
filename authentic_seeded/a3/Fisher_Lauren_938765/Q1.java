import java.util.*;
public class Q1 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter size: ");
        int n = 0;
        if (x.hasNextInt()) n = x.nextInt();
        if (n < 0) n = 0;
        int[] y = new int[n];
        System.out.print("Enter elements: ");
        int i = 0;
        while (i < n) {
            if (x.hasNextInt()) {
                int t = x.nextInt();
                y[i] = t;
            }
            i++;
        }
        System.out.print("Enter target: ");
        int z = 0;
        if (x.hasNextInt()) z = x.nextInt();
        int a = -1;
        int j = 0;
        if (n != 0) {
            while (j < n && a == -1) {
                int u = y[j];
                if (u == z) a = j;
                j++;
            }
        }
        System.out.print("Found at index: ");
        System.out.print(a);
    }
}