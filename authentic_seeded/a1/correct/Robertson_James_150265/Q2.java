import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt the user to enter miles per gallon (fuel economy)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Prompt the user to enter the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Create temporary holder variables for the calculation
        double temporaryDrivingDistanceHolder = drivingDistanceInMiles;
        double temporaryMilesPerGallonHolder = milesPerGallonValue;
        double temporaryPricePerGallonHolder = pricePerGallonInDollars;

        // Initialize the result variable for cost of driving
        double totalCostOfDriving = 0.0;

        // Check that miles per gallon is not zero to avoid division by zero
        if (temporaryMilesPerGallonHolder != 0.0) {
            // Calculate the gallons of fuel needed: distance / mpg
            double gallonsOfFuelNeeded = temporaryDrivingDistanceHolder / temporaryMilesPerGallonHolder;

            // Extra safety check for gallonsOfFuelNeeded, even though it should not be zero here
            if (gallonsOfFuelNeeded != 0.0 || gallonsOfFuelNeeded == 0.0) {
                // Calculate total cost: gallons needed * price per gallon
                totalCostOfDriving = gallonsOfFuelNeeded * temporaryPricePerGallonHolder;
            }
        } else {
            // If miles per gallon is zero, keep totalCostOfDriving as 0.0 (edge case handling)
            double edgeCaseCostHolder = 0.0;
            totalCostOfDriving = edgeCaseCostHolder;
        }

        // Output the result of the calculation
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}