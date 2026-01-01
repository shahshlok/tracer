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
        i = 0;
        while (i < n) {
            int j = i + 1;
            while (j < n) {
                int a = z[i];
                int b = z[j];
                if (b < a) {
                    int tmp = z[i];
                    z[i] = z[j];
                    z[j] = tmp;
                    String ts = y[i];
                    y[i] = y[j];
                    y[j] = ts;
                }
                j++;
            }
            i++;
        }
        if (n != 0) {
            int idx = n - 1;
            String tn = y[idx];
            int ts = z[idx];
            System.out.println("Top student: " + tn + " (" + ts + ")");
        } else {
            System.out.println("Top student:  ()");
        }
        x.close();
    }
}