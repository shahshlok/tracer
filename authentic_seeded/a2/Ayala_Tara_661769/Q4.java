import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter height: ");
        int n = scanner.nextInt();

        int i = 1;
        while (i <= n) {
            int j = 1;
            while (j <= i) {
                System.out.print("*");
                i++;
            }
            System.out.println();
            i++;
        }
    }
}