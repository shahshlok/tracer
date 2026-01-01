import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt the user for the miles per gallon value
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Prompt the user for the fuel price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double fuelPricePerGallonInDollars = userInputScanner.nextDouble();

        // Declare intermediate variables for the formula components
        // Formula: cost = (distance / mpg) * price
        double a_distanceDividedByMilesPerGallon;
        double b_costOfDrivingInDollars;

        // Compute the number of gallons of fuel needed using a = distance / mpg
        a_distanceDividedByMilesPerGallon = drivingDistanceInMiles / milesPerGallonFuelEconomy;

        // Compute the cost of driving using b = a * price
        b_costOfDrivingInDollars = a_distanceDividedByMilesPerGallon * fuelPricePerGallonInDollars;

        // Display the final cost of driving, matching the required output format
        System.out.println("The cost of driving is $" + b_costOfDrivingInDollars);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}