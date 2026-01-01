import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user for the fuel economy in miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Ask the user for the price of fuel in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Declare intermediate math variables to represent the formula parts
        // Formula for cost of driving: cost = (distance / mpg) * price
        double a = drivingDistanceInMiles;           // a represents the total distance
        double b = milesPerGallonFuelEconomy;        // b represents miles per gallon
        double c = pricePerGallonInDollars;          // c represents price per gallon

        // First compute the number of gallons needed: distance / mpg
        double numberOfGallonsNeeded = a / b;

        // Then compute the total cost: gallons needed * price per gallon
        double totalCostOfDriving = numberOfGallonsNeeded * c;

        // Display the final cost of driving
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}