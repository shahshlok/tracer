import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        double v0 = 0;
        double v1 = 0;
        double t = 0;

        double a = v1 - v0;
        double averageAcceleration = a / t;

        System.out.print("Enter v0, v1, and t: ");
        v0 = input.nextDouble();
        v1 = input.nextDouble();
        t = input.nextDouble();

        System.out.println("The average acceleration is " + averageAcceleration);

        input.close();
    }
}