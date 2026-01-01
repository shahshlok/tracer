import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int n = scanner.nextInt();

        if (n > 0) {
            int row = 1;
            while (row <= n) {
                int starsInRow = row;
                if (starsInRow > 0) {
                    int col = 1;
                    while (col <= starsInRow) {
                        System.out.print("*");
                        col = col + 1;
                    }
                }
                System.out.println();
                row = row + 1;
            }
        }

        scanner.close();
    }
}