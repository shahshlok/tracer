import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt the user to enter the miles per gallon value
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Prompt the user to enter the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Declare intermediate math variables a, b, c to structure the formula
        // Formula: cost = (distance / mpg) * price
        double a = drivingDistanceInMiles;          // a represents the driving distance
        double b = milesPerGallonFuelEconomy;       // b represents miles per gallon
        double c = pricePerGallonInDollars;         // c represents price per gallon

        // Compute the number of gallons needed: distance divided by miles per gallon
        double numberOfGallonsNeeded = a / b;

        // Compute the total cost of the road trip: gallons needed times price per gallon
        double totalCostOfDriving = numberOfGallonsNeeded * c;

        // Display the result to the user in the requested format
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}