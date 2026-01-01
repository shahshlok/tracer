import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter v0, v1, and t: ");
        double v0 = scanner.nextDouble();
        double v1 = scanner.nextDouble();
        double t = scanner.nextDouble();

        double timeHolder = t;
        double acceleration = 0.0;

        if (timeHolder != 0.0) {
            double velocityChange = v1 - v0;
            acceleration = velocityChange / timeHolder;
        } else {
            acceleration = 0.0;
        }

        System.out.println("The average acceleration is " + acceleration);

        scanner.close();
    }
}