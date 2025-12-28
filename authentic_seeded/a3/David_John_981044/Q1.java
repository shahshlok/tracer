import java.util.*;
public class Q1 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter size: ");
        int n = x.nextInt();
        if (n < 0) n = 0;
        int[] y = new int[n];
        System.out.print("Enter elements: ");
        int i = 0;
        while (i < n) {
            y[i] = x.nextInt();
            i++;
        }
        System.out.print("Enter target: ");
        int t = x.nextInt();
        int a = -1;
        int j = 0;
        while (j < n) {
            int z = y[j];
            if (z == t && a == -1) a = j;
            j++;
        }
        System.out.print("Found at index: ");
        System.out.println(a);
    }
}