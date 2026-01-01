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
        int i = 0;
        while (i < n) {
            String t = "";
            if (s.hasNext()) t = s.next();
            x[i] = t;
            i++;
        }
        System.out.print("Enter scores: ");
        i = 0;
        while (i < n) {
            int t = 0;
            if (s.hasNextInt()) t = s.nextInt();
            y[i] = t;
            i++;
        }
        i = 0;
        while (i < n) {
            int j = 0;
            while (j < n - 1) {
                int c1 = y[j];
                int c2 = y[j + 1];
                if (c1 > c2) {
                    int ti = y[j];
                    y[j] = y[j + 1];
                    y[j + 1] = ti;
                    String ts = x[j];
                    x[j] = x[j + 1];
                    x[j + 1] = ts;
                }
                j++;
            }
            i++;
        }
        String r1 = "";
        int r2 = 0;
        if (n != 0) {
            r1 = x[n - 1];
            r2 = y[n - 1];
        }
        System.out.print("Top student: ");
        System.out.print(r1);
        System.out.print(" (");
        System.out.print(r2);
        System.out.print(")");
    }
}