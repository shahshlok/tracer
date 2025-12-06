import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Initialize variables to hold the user input values
        double drivingDistanceInMiles = 0.0;
        double milesPerGallonFuelEconomy = 0.0;
        double pricePerGallonInDollars = 0.0;

        // Prompt the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        // Call nextDouble so the program can read and remember the value
        keyboardInputScanner.nextDouble();

        // Prompt the user to enter the fuel economy in miles per gallon
        System.out.print("Enter miles per gallon: ");
        // Call nextDouble so the program can read and remember the value
        keyboardInputScanner.nextDouble();

        // Prompt the user to enter the fuel price per gallon
        System.out.print("Enter price in $ per gallon: ");
        // Call nextDouble so the program can read and remember the value
        keyboardInputScanner.nextDouble();

        // Initialize a variable to hold the cost of driving
        double costOfDrivingInDollars = 0.0;

        // Use temporary variables to make the calculation steps more explicit
        double temporaryHolderForDistance = drivingDistanceInMiles;
        double temporaryHolderForMpg = milesPerGallonFuelEconomy;
        double temporaryHolderForPrice = pricePerGallonInDollars;

        // Check that miles per gallon is not zero to avoid division by zero
        if (temporaryHolderForMpg != 0.0) {
            // Calculate how many gallons of fuel will be used
            double gallonsOfFuelUsed = temporaryHolderForDistance / temporaryHolderForMpg;

            // Calculate the cost of driving by multiplying gallons used by price per gallon
            costOfDrivingInDollars = gallonsOfFuelUsed * temporaryHolderForPrice;
        } else {
            // If miles per gallon is zero, we will keep cost as 0.0 (although this is an odd case)
            costOfDrivingInDollars = 0.0;
        }

        // Print the final cost of driving in the exact required format
        System.out.println("The cost of driving is $" + costOfDrivingInDollars);

        // Close the scanner to be safe
        keyboardInputScanner.close();
    }
}