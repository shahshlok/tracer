import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user to enter the fuel efficiency in miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEfficiency = userInputScanner.nextDouble();

        // Ask the user to enter the price of fuel in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double fuelPriceDollarsPerGallon = userInputScanner.nextDouble();

        // Declare intermediate math variables
        double a; // will represent the number of gallons of fuel needed
        double b; // will represent the total cost of fuel
        double c; // will represent the final cost of driving (same as b, but kept for formula style)

        // Compute the number of gallons needed using the formula:
        // a = distance / mpg
        a = drivingDistanceInMiles / milesPerGallonFuelEfficiency;

        // Compute the total fuel cost using the formula:
        // b = a * pricePerGallon
        b = a * fuelPriceDollarsPerGallon;

        // Assign the final cost to c to emphasize the formula structure
        c = b;

        // Display the cost of driving using the required output format
        System.out.println("The cost of driving is $" + c);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}