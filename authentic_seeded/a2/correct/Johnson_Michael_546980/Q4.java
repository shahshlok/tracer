import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int n = scanner.nextInt();

        if (n > 0) {
            int row = 1;
            while (row <= n) {
                int starsToPrint = row;
                if (starsToPrint > 0) {
                    int count = 0;
                    while (count < starsToPrint) {
                        System.out.print("*");
                        count = count + 1;
                    }
                }
                System.out.println();
                row = row + 1;
            }
        }

        scanner.close();
    }
}