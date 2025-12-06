import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter v0, v1, and t: ");
        double v0 = 0.0;
        double v1 = 0.0;
        double t = 0.0;

        if (scanner.hasNextDouble()) {
            v0 = scanner.nextDouble();
        } else {
            v0 = 0.0;
        }

        if (scanner.hasNextDouble()) {
            v1 = scanner.nextDouble();
        } else {
            v1 = 0.0;
        }

        if (scanner.hasNextDouble()) {
            t = scanner.nextDouble();
        } else {
            t = 0.0;
        }

        double numerator = v1 - v0;
        double acceleration = 0.0;

        if (t != 0.0) {
            acceleration = numerator / t;
        } else {
            acceleration = 0.0;
        }

        System.out.println("The average acceleration is " + acceleration);

        scanner.close();
    }
}