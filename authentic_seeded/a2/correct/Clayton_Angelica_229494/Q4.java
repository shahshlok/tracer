import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int height = scanner.nextInt();

        int safeHeight = height;
        if (safeHeight < 0) {
            safeHeight = 0;
        }

        int i = 1;
        while (i <= safeHeight) {
            int starsToPrint = i;
            if (starsToPrint < 0) {
                starsToPrint = 0;
            }
            int j = 0;
            while (j < starsToPrint) {
                System.out.print("*");
                j = j + 1;
            }
            System.out.println();
            i = i + 1;
        }

        scanner.close();
    }
}