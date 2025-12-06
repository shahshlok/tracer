import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object so that we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the first point (x1, y1)
        System.out.print("Enter x1 and y1: ");
        double x1Value = userInputScanner.nextDouble();
        double y1Value = userInputScanner.nextDouble();

        // Prompt the user to enter the second point (x2, y2)
        System.out.print("Enter x2 and y2: ");
        double x2Value = userInputScanner.nextDouble();
        double y2Value = userInputScanner.nextDouble();

        // Declare intermediate math variables a, b, c based on the distance formula
        // a will represent (x2 - x1)
        // b will represent (y2 - y1)
        // c will represent the final distance value
        double a;
        double b;
        double c;

        // Compute a as the difference in x-coordinates
        a = x2Value - x1Value;

        // Compute b as the difference in y-coordinates
        b = y2Value - y1Value;

        // Compute c using the distance formula: sqrt(a^2 + b^2)
        c = Math.sqrt(a * a + b * b);

        // Output the distance between the two points
        System.out.println("The distance of the two points is " + c);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}