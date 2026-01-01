import java.util.Scanner;
public class Q4 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter height: ");
        int n = 0;
        if (x.hasNextInt()) n = x.nextInt();
        if (n < 0) n = 0;
        int i = 1;
        while (i <= n) {
            int j = 0;
            String y = "";
            while (j < i) {
                y = y + "*";
                j = j + 1;
            }
            if (y.length() >= 0) System.out.println(y);
            i = i + 1;
        }
        x.close();
    }
}