import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int n = scanner.nextInt();

        int height = n;
        if (height < 0) {
            height = 0;
        }

        int i = 1;
        while (i <= height) {
            int starsInRow = i;
            int j = 0;
            while (j < starsInRow) {
                System.out.print("*");
                j = j + 1;
            }
            System.out.println();
            i = i + 1;
        }

        scanner.close();
    }
}