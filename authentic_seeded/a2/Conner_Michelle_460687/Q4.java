import java.util.Scanner;
public class Q4 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter height: ");
        int n = 0;
        if (s != null) n = s.nextInt();
        int x = 1;
        if (n > 0) {
            while (x <= n) {
                int y = 0;
                String z = "";
                if (x >= 1) {
                    while (y < x) {
                        z = z + "*";
                        y = y + 1;
                    }
                }
                System.out.println(z);
                x = x + 1;
            }
        }
    }
}