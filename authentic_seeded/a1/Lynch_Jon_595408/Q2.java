import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceMiles = userInputScanner.nextDouble();

        // Ask the user to enter the fuel efficiency in miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Ask the user to enter the price of fuel per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Now use math to calculate the cost of the road trip
        // Formula: cost = (distance / mpg) * pricePerGallon

        // a will represent the fuel used in gallons = distance / mpg
        double a = drivingDistanceMiles / milesPerGallonFuelEconomy;

        // b will represent the price per gallon
        double b = pricePerGallonInDollars;

        // c will represent the total cost of the trip = a * b
        double c = a * b;

        // Print the result following the sample output format
        System.out.println("The cost of driving is $" + c);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}