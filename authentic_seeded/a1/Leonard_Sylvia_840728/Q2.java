import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Initialize the driving distance in miles to 0 so the formula is ready
        double drivingDistanceInMiles = 0.0;

        // Initialize the fuel efficiency in miles per gallon to 0 so the formula is ready
        double milesPerGallonFuelEfficiency = 0.0;

        // Initialize the price of fuel in dollars per gallon to 0 so the formula is ready
        double fuelPriceDollarsPerGallon = 0.0;

        // Declare intermediate math variables a, b, c to structure the formula
        // Formula: cost = (distance / mpg) * price
        double a = drivingDistanceInMiles;                 // a represents the distance
        double b = milesPerGallonFuelEfficiency;           // b represents the miles per gallon
        double c = fuelPriceDollarsPerGallon;              // c represents the price per gallon

        // Compute the number of gallons used: distance divided by miles per gallon
        double numberOfGallonsUsed = a / b;

        // Compute the total cost: gallons used multiplied by price per gallon
        double totalCostOfDriving = numberOfGallonsUsed * c;

        // Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user to enter the fuel efficiency in miles per gallon
        System.out.print("Enter miles per gallon: ");
        milesPerGallonFuelEfficiency = userInputScanner.nextDouble();

        // Ask the user to enter the price of fuel in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        fuelPriceDollarsPerGallon = userInputScanner.nextDouble();

        // Display the result following the required format
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}