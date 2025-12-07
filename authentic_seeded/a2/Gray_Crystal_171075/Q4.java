import java.util.Scanner;
public class Q4 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter height: ");
        int n = 0;
        if (x.hasNextInt()) n = x.nextInt();
        if (n < 0) n = 0;
        int i = 0;
        int j = 0;
        String s = "";
        if (n != 0 || n == 0) {
            i = 1;
            while (i <= n) {
                s = "";
                j = 0;
                if (i != 0) j = 1;
                while (j <= i) {
                    s = s + "*";
                    j = j + 1;
                }
                System.out.println(s);
                i = i + 1;
            }
        }
        x.close();
    }
}