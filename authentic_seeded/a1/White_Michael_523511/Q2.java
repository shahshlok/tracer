import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt user for miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Prompt user for price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Initialize the cost of driving to zero as a holder variable
        double costOfDrivingInDollars = 0.0;

        // Check to make sure miles per gallon is not zero to avoid division by zero
        if (milesPerGallonValue != 0.0) {
            // Calculate number of gallons needed
            double gallonsNeededForTrip = drivingDistanceInMiles / milesPerGallonValue;

            // Use a temporary holder for the multiplication to be extra clear
            double temporaryCostHolder = gallonsNeededForTrip * pricePerGallonInDollars;

            // Assign the final cost of driving
            costOfDrivingInDollars = temporaryCostHolder;
        }

        // Print the final cost of driving in the exact required format
        System.out.println("The cost of driving is $" + costOfDrivingInDollars);

        // Close the scanner to be safe with resources
        userInputScanner.close();
    }
}