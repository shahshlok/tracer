import java.util.*;
public class Q2 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter number of students: ");
        int n = 0;
        if (s.hasNextInt()) n = s.nextInt();
        if (n < 0) n = 0;
        String[] x = new String[n];
        int[] y = new int[n];
        System.out.print("Enter names: ");
        for (int i = 0; i < n; i++) {
            String t = "";
            if (s.hasNext()) t = s.next();
            x[i] = t;
        }
        System.out.print("Enter scores: ");
        for (int i = 0; i < n; i++) {
            int t = 0;
            if (s.hasNextInt()) t = s.nextInt();
            y[i] = t;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j + 1 < n; j++) {
                int t = y[j];
                if (y[j] > y[j + 1]) {
                    t = y[j];
                    y[j] = y[j + 1];
                    y[j + 1] = t;
                    String u = x[j];
                    x[j] = x[j + 1];
                    x[j + 1] = u;
                }
            }
        }
        String r = "";
        int v = 0;
        if (n > 0) {
            r = x[n - 1];
            v = y[n - 1];
        }
        System.out.print("Top student: ");
        if (r != null) System.out.print(r);
        System.out.print(" (");
        System.out.print(v);
        System.out.print(")");
    }
}