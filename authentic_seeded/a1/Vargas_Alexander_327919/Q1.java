import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double v0 = 0;
        double v1 = 0;
        double t = 0;

        double acceleration = (v1 - v0) / t;

        System.out.print("Enter v0, v1, and t: ");
        v0 = scanner.nextDouble();
        v1 = scanner.nextDouble();
        t = scanner.nextDouble();

        System.out.println("The average acceleration is " + acceleration);

        scanner.close();
    }
}