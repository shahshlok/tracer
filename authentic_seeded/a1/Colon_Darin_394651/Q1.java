import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter v0, v1, and t: ");
        double v0 = 0.0;
        double v1 = 0.0;
        double t = 0.0;

        scanner.nextDouble();
        scanner.nextDouble();
        scanner.nextDouble();

        double timeHolder = t;
        double acceleration = 0.0;

        if (timeHolder != 0.0) {
            double numerator = v1 - v0;
            double denominator = timeHolder;
            acceleration = numerator / denominator;
        }

        System.out.println("The average acceleration is " + acceleration);

        scanner.close();
    }
}