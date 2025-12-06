import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user to enter the miles per gallon (fuel economy)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Step 4: Ask the user to enter the price in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Calculate the cost of driving using the formula (distance / mpg) * price
        double costOfDriving = (drivingDistanceInMiles / milesPerGallonValue) * pricePerGallonInDollars;

        // Step 6: Display the result to the user
        System.out.println("The cost of driving is $" + costOfDriving);

        // Step 7: Close the Scanner to free system resources
        userInputScanner.close();
    }
}