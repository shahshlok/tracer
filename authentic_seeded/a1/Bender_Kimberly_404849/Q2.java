import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user to enter the miles per gallon value
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Ask the user to enter the price per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Create temporary holder variables for safety and clarity
        double safeDrivingDistanceInMiles = drivingDistanceInMiles;
        double safeMilesPerGallonValue = milesPerGallonValue;
        double safePricePerGallonInDollars = pricePerGallonInDollars;

        // Initialize a variable to hold the total cost of driving
        double totalCostOfDriving = 0.0;

        // Extra defensive checks, even if not strictly necessary
        if (safeMilesPerGallonValue != 0) {
            // Calculate the number of gallons needed for the trip
            double gallonsNeededForTrip = safeDrivingDistanceInMiles / safeMilesPerGallonValue;

            // Another temporary variable for gallons, just to be extra sure
            double safeGallonsNeededForTrip = gallonsNeededForTrip;

            // Check price is not zero, even though zero could be allowed logically
            if (safePricePerGallonInDollars != 0 || safePricePerGallonInDollars == 0) {
                // Calculate the total cost of the driving trip
                totalCostOfDriving = safeGallonsNeededForTrip * safePricePerGallonInDollars;
            }
        }

        // Print the final result exactly as required
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Close the scanner to be polite, even though the program is ending
        userInputScanner.close();
    }
}