import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user for miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Ask the user for the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double fuelPricePerGallonInDollars = userInputScanner.nextDouble();

        // Declare intermediate math variables to follow the formula: (distance / mpg) * price
        double a = drivingDistanceInMiles;              // a represents the distance
        double b = milesPerGallonFuelEconomy;           // b represents miles per gallon
        double c = fuelPricePerGallonInDollars;         // c represents price per gallon

        // First compute a divided by b, which is the number of gallons needed
        double gallonsOfFuelNeeded = a / b;

        // Then multiply the gallons needed by c, the price per gallon, to get the total cost
        double totalDrivingCostInDollars = gallonsOfFuelNeeded * c;

        // Display the final cost of driving
        System.out.println("The cost of driving is $" + totalDrivingCostInDollars);

        // Close the scanner to free the resource
        userInputScanner.close();
    }
}