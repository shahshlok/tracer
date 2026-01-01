import java.util.*;
public class Q4 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter height: ");
        int n = 0;
        if (s.hasNextInt()) n = s.nextInt();
        if (n < 0) n = 0;
        int x = 0;
        int y = 0;
        while (x < n) {
            y = 0;
            int t = x + 1;
            if (t < 0) t = 0;
            while (y < t) {
                System.out.print("*");
                y = y + 1;
            }
            System.out.println();
            x = x + 1;
        }
    }
}