import java.util.Scanner;

public class DistanceBetweenPoints {

    public static void main(String[] args) {

        // create scanner object to read input from the user
        Scanner input = new Scanner(System.in);

        // declare variables for the coordinates of the two points
        double x1;
        double y1;
        double x2;
        double y2;

        // prompt user for the first point (x1, y1)
        System.out.print("Enter x1 and y1: ");
        x1 = input.nextDouble();
        y1 = input.nextDouble();

        // prompt user for the second point (x2, y2)
        System.out.print("Enter x2 and y2: ");
        x2 = input.nextDouble();
        y2 = input.nextDouble();

        // compute the differences between the coordinates
        double x_difference = x2 - x1;
        double y_difference = y2 - y1;

        // compute the squares of the differences
        double x_difference_squared = x_difference * x_difference;
        double y_difference_squared = y_difference * y_difference;

        // compute the sum of the squares
        double sum_of_squares = x_difference_squared + y_difference_squared;

        // compute the distance using the square root of the sum of squares
        double distance = Math.sqrt(sum_of_squares);

        // display the result to the user
        System.out.println("The distance of the two points is " + distance);

        // close the scanner
        input.close();
    }
}
