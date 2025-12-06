import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt the user to enter the fuel efficiency in miles per gallon
        System.out.print("Enter miles per gallon: ");
        double fuelEfficiencyMilesPerGallon = userInputScanner.nextDouble();

        // Prompt the user to enter the price of fuel in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double fuelPriceDollarsPerGallon = userInputScanner.nextDouble();

        // Declare intermediate math variables for the formula (distance / mpg) * price
        double a = drivingDistanceInMiles;              // a represents the total distance in miles
        double b = fuelEfficiencyMilesPerGallon;        // b represents the miles per gallon
        double c = fuelPriceDollarsPerGallon;           // c represents the price per gallon in dollars

        // First compute how many gallons of fuel are needed: distance / mpg
        double gallonsOfFuelNeeded = a / b;

        // Then compute the total cost of the trip: gallons * price per gallon
        double totalCostOfDriving = gallonsOfFuelNeeded * c;

        // Display the result to the user, matching the required format
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}