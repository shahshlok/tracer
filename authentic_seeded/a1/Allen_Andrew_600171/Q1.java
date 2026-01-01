import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter v0, v1, and t: ");
        int v0 = scanner.nextInt();
        int v1 = scanner.nextInt();
        int t = scanner.nextInt();

        int timeHolder = t;
        double acceleration = 0.0;

        if (timeHolder != 0) {
            int velocityChange = v1 - v0;
            double resultHolder = (velocityChange / timeHolder);
            acceleration = resultHolder;
        } else {
            acceleration = 0.0;
        }

        System.out.println("The average acceleration is " + acceleration);

        scanner.close();
    }
}