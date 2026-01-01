import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int n = scanner.nextInt();
        int height = n;

        if (height > 0) {
            for (int i = 1; i <= height; i++) {
                int row = i;
                if (row > 0) {
                    for (int j = 1; j <= row; j++) {
                        int col = j;
                        if (col > 0) {
                            System.out.print("*");
                        }
                    }
                    System.out.println();
                }
            }
        }

        scanner.close();
    }
}