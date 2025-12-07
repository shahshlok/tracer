import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int height = scanner.nextInt();

        if (height > 0) {
            int row = 1;
            while (row <= height) {
                int starsInRow = row;
                int starCounter = 0;

                while (starCounter < starsInRow) {
                    System.out.print("*");
                    starCounter = starCounter + 1;
                }

                System.out.println();
                row = row + 1;
            }
        }

        scanner.close();
    }
}