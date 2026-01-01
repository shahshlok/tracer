import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter height: ");
        int n = scanner.nextInt();

        if (n > 0) {
            int row = 1;
            while (row <= n) {
                int col = 1;
                String line = "";
                while (col <= row) {
                    line = line + "*";
                    col = col + 1;
                }
                System.out.println(line);
                row = row + 1;
            }
        }

        scanner.close();
    }
}