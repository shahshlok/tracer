import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            int a = i;
            for (int j = 1; j <= a; j++) {
                System.out.print("*");
            }
            System.out.println();
        }

        scanner.close();
    }
}