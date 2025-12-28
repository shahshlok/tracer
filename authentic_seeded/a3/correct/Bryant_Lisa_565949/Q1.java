import java.util.*;
public class Q1 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter size: ");
        int n = 0;
        if (s.hasNextInt()) n = s.nextInt();
        int[] x = new int[0];
        if (n >= 0) x = new int[n];
        System.out.print("Enter elements: ");
        int i = 0;
        while (i < n) {
            if (s.hasNextInt()) x[i] = s.nextInt();
            i++;
        }
        System.out.print("Enter target: ");
        int t = 0;
        if (s.hasNextInt()) t = s.nextInt();
        int y = -1;
        int j = 0;
        while (j < n) {
            int z = x[j];
            if (z == t && y == -1) {
                y = j;
            }
            j++;
        }
        System.out.print("Found at index: ");
        System.out.print(y);
    }
}