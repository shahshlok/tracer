import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int n = scanner.nextInt();

        if (n > 0) {
            int i = 1;
            while (i <= n) {
                int starsToPrint = i;
                if (starsToPrint > 0) {
                    int j = 0;
                    while (j < starsToPrint) {
                        System.out.print("*");
                        j = j + 1;
                    }
                }
                System.out.println();
                i = i + 1;
            }
        }

        scanner.close();
    }
}