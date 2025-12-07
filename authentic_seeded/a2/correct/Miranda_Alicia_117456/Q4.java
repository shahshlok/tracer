import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int height = scanner.nextInt();

        if (height > 0) {
            int currentRow = 1;
            while (currentRow <= height) {
                int starsInRow = currentRow;
                int starCounter = 0;

                while (starCounter < starsInRow) {
                    System.out.print("*");
                    starCounter = starCounter + 1;
                }

                System.out.println();
                currentRow = currentRow + 1;
            }
        }

        scanner.close();
    }
}