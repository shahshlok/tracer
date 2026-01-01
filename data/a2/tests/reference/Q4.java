import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter height: ");
        int height = scanner.nextInt();

        for (int row = 1; row <= height; row++) {
            for (int star = 0; star < row; star++) {
                System.out.print("*");
            }
            System.out.println();
        }

        scanner.close();
    }
}
