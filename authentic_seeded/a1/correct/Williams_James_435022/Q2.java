import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceMiles = userInputScanner.nextDouble();

        // Ask the user for the miles per gallon value (fuel economy)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Ask the user for the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonDollars = userInputScanner.nextDouble();

        // Declare intermediate math variables to clearly show the formula parts
        double a = drivingDistanceMiles;       // a represents the distance
        double b = milesPerGallonValue;        // b represents the fuel economy
        double c = pricePerGallonDollars;      // c represents the price per gallon

        // Apply the formula: (distance / mpg) * price
        // First compute the gallons of fuel needed: distance / mpg
        double gallonsOfFuelNeeded = a / b;

        // Then compute the cost: gallons needed * price per gallon
        double totalDrivingCost = gallonsOfFuelNeeded * c;

        // Display the result with the exact wording shown in the sample run
        System.out.println("The cost of driving is $" + totalDrivingCost);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}