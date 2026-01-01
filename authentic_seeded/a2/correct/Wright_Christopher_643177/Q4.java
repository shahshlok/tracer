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

        int row = 1;
        while (row <= height) {
            int starCount = row;
            int starIndex = 0;
            String line = "";
            while (starIndex < starCount) {
                line = line + "*";
                starIndex = starIndex + 1;
            }
            System.out.println(line);
            row = row + 1;
        }

        scanner.close();
    }
}