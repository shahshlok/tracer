import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int n = scanner.nextInt();

        int height = n;

        if (height > 0) {
            for (int row = 1; row < height; row++) {
                int starsInRow = row;
                int starCount = 0;
                while (starCount < starsInRow) {
                    System.out.print("*");
                    starCount = starCount + 1;
                }
                System.out.println();
            }
        }

        scanner.close();
    }
}