import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object for reading user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt the user for the miles per gallon (fuel economy)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Prompt the user for the fuel price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double fuelPricePerGallonInDollars = userInputScanner.nextDouble();

        // Create temporary holder variables just to be extra clear and safe
        double safeDrivingDistanceInMiles = drivingDistanceInMiles;
        double safeMilesPerGallonValue = milesPerGallonValue;
        double safeFuelPricePerGallonInDollars = fuelPricePerGallonInDollars;

        // Initialize the total cost of driving to zero
        double totalCostOfDriving = 0.0;

        // Extra checks to be nervous about edge cases
        if (safeMilesPerGallonValue != 0) {
            // Calculate fuel used using distance divided by miles per gallon
            double fuelUsedInGallons = safeDrivingDistanceInMiles / safeMilesPerGallonValue;

            // Extra check to make sure fuel price is not something strange
            if (safeFuelPricePerGallonInDollars != 0 || safeFuelPricePerGallonInDollars == 0) {
                // Calculate the total cost of driving
                totalCostOfDriving = fuelUsedInGallons * safeFuelPricePerGallonInDollars;
            }
        }

        // Print the cost of driving in the exact format required
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Close the scanner to be polite to system resources
        userInputScanner.close();
    }
}