import java.util.Scanner;
public class Q4 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter height: ");
        int n = 0;
        if (x.hasNextInt()) n = x.nextInt();
        if (n < 0) n = 0;
        int i = 1;
        while (i <= n) {
            int j = 0;
            int y = i;
            if (y != 0) {
                while (j < y) {
                    System.out.print("*");
                    j++;
                }
            }
            System.out.println();
            i++;
        }
    }
}