import java.util.*;
public class Q4 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter size: ");
        int n = 0;
        if (x.hasNextInt()) n = x.nextInt();
        if (n < 0) n = 0;
        int[] y = new int[n];
        System.out.print("Enter elements: ");
        int i = 0;
        while (i < n) {
            if (x.hasNextInt()) y[i] = x.nextInt();
            i++;
        }
        System.out.print("Shifted: ");
        if (n > 0) {
            int t = y[n - 1];
            i = n - 1;
            while (i > 0) {
                int u = y[i - 1];
                y[i] = u;
                i--;
            }
            y[0] = t;
        }
        i = 0;
        while (i < n) {
            System.out.print(y[i]);
            if (i + 1 < n) System.out.print(" ");
            i++;
        }
    }
}