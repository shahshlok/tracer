import java.util.*;
public class Q2 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter number of students: ");
        int n = x.nextInt();
        if (n < 0) n = 0;
        String[] y = new String[n];
        int[] z = new int[n];
        System.out.print("Enter names: ");
        int i = 0;
        while (i < n) {
            String t = x.next();
            y[i] = t;
            i++;
        }
        System.out.print("Enter scores: ");
        i = 0;
        while (i < n) {
            int t2 = x.nextInt();
            z[i] = t2;
            i++;
        }
        int a = 0;
        while (a < n) {
            int b = 0;
            while (b < n - 1) {
                int s1 = z[b];
                int s2 = z[b + 1];
                if (s1 > s2) {
                    int th = z[b];
                    z[b] = z[b + 1];
                    z[b + 1] = th;
                    String ths = y[b];
                    y[b] = y[b + 1];
                    y[b + 1] = ths;
                }
                b++;
            }
            a++;
        }
        if (n == 0) {
            System.out.print("Top student: ");
        } else {
            int idx = n - 1;
            String nm = y[idx];
            int sc = z[idx];
            System.out.print("Top student: " + nm + " (" + sc + ")");
        }
    }
}