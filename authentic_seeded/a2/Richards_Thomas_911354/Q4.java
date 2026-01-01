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
            for (int x = 1; x < n; x = x + 1) {
                int y = 0;
                String t = "";
                if (x >= 1) {
                    for (y = 0; y < x; y = y + 1) {
                        String u = "*";
                        if (u != null) {
                            t = t + u;
                        }
                    }
                }
                System.out.println(t);
            }
        }
    }
}