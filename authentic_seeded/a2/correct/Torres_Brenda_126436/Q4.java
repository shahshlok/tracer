import java.util.Scanner;
public class Q4 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter height: ");
        int n = 0;
        if (x.hasNextInt()) n = x.nextInt();
        if (n < 0) n = 0;
        int y = 1;
        while (y <= n) {
            int z = 0;
            String s = "";
            while (z < y) {
                s = s + "*";
                z = z + 1;
            }
            if (s.length() >= 0) System.out.println(s);
            y = y + 1;
        }
        x.close();
    }
}