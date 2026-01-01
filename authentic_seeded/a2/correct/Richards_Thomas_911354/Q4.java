import java.util.Scanner;
public class Q4 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter height: ");
        int n = 0;
        if (s != null) {
            n = s.nextInt();
        }
        if (n > 0) {
            int x = 1;
            while (x <= n) {
                int y = 0;
                String t = "";
                if (x >= 1) {
                    while (y < x) {
                        t = t + "*";
                        y = y + 1;
                    }
                }
                System.out.println(t);
                x = x + 1;
            }
        }
    }
}