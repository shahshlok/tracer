import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int n = scanner.nextInt();

        if (n > 0) {
            for (int row = 1; row < n; row++) {
                int starsToPrint = row;
                if (starsToPrint > 0) {
                    for (int count = 0; count < starsToPrint; count++) {
                        System.out.print("*");
                    }
                }
                System.out.println();
            }
        }

        scanner.close();
    }
}