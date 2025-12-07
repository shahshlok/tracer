import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int height = scanner.nextInt();

        if (height > 0) {
            int row = 1;
            while (row <= height) {
                int starsToPrint = row;
                if (starsToPrint > 0) {
                    int starCount = 0;
                    while (starCount < starsToPrint) {
                        System.out.print("*");
                        starCount = starCount + 1;
                    }
                }
                System.out.println();
                row = row + 1;
            }
        }

        scanner.close();
    }
}