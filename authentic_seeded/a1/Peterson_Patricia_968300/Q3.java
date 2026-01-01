import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter x1 and y1: ");
        int x1 = scanner.nextInt();
        int y1 = scanner.nextInt();

        System.out.print("Enter x2 and y2: ");
        int x2 = scanner.nextInt();
        int y2 = scanner.nextInt();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int distance = (dx ^ 2) + (dy ^ 2);

        System.out.println("The distance of the two points is " + distance);

        scanner.close();
    }
}