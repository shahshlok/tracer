import java.util.Scanner;

public class AccelerationCalculator {

    public static void main(String[] args) {

        // create a Scanner object to read input from the keyboard
        Scanner input = new Scanner(System.in);

        // prompt the user to enter the starting velocity, final velocity, and time
        System.out.print("Enter v0, v1, and t: ");

        // read the starting velocity v0 from the user
        double v0 = input.nextDouble();

        // read the final velocity v1 from the user
        double v1 = input.nextDouble();

        // read the time t from the user
        double t = input.nextDouble();

        // compute the average acceleration using the formula (v1 - v0) / t
        double average_acceleration = (v1 - v0) / t;

        // display the result of the average acceleration to the user
        System.out.println("The average acceleration is " + average_acceleration);

        // close the Scanner object to prevent resource leaks
        input.close();
    }
}
