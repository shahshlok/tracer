import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt the user for the miles per gallon (fuel economy)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Prompt the user for the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Initialize a variable to hold the cost of driving
        double costOfDriving = 0.0;

        // Check to make sure milesPerGallonValue is not zero to avoid division by zero
        if (milesPerGallonValue != 0) {
            // Calculate gallons used as distance divided by miles per gallon
            double gallonsUsedForTrip = drivingDistanceInMiles / milesPerGallonValue;

            // Calculate the total cost as gallons used multiplied by price per gallon
            double temporaryCostHolder = gallonsUsedForTrip * pricePerGallonInDollars;

            // Store the result in the costOfDriving variable
            costOfDriving = temporaryCostHolder;
        }

        // Print the final result to the user
        System.out.println("The cost of driving is $" + costOfDriving);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}