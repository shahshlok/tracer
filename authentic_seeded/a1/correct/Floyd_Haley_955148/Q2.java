import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user to enter the miles per gallon value
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Ask the user to enter the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Now we will calculate the cost using the formula:
        // cost = (distance / mpg) * price
        // First declare intermediate math variables as requested

        // a represents the distance divided by miles per gallon (this gives gallons used)
        double a = drivingDistanceInMiles / milesPerGallonFuelEconomy;

        // b represents the price per gallon (already given)
        double b = pricePerGallonInDollars;

        // c will represent the final cost of the trip using the formula c = a * b
        double c = a * b;

        // Print the final cost of driving using the exact format from the sample run
        System.out.println("The cost of driving is $" + c);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}