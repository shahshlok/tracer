import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user to enter the car's fuel efficiency in miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Ask the user to enter the price of fuel in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Calculate how many gallons of fuel will be used for the trip
        double gallonsOfFuelUsed = drivingDistanceInMiles / milesPerGallonValue;

        // Calculate the total cost of the trip by multiplying gallons used by price per gallon
        double totalCostOfDriving = gallonsOfFuelUsed * pricePerGallonInDollars;

        // Display the total cost of the road trip to the user
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Close the scanner because we are done reading input
        userInputScanner.close();
    }
}