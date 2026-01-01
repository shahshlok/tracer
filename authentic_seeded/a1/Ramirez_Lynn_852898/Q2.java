import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Set up all the variables we will need and initialize them to 0
        double drivingDistanceInMiles = 0.0;
        double milesPerGallonValue = 0.0;
        double pricePerGallonInDollars = 0.0;

        // Step 3: Calculate the total cost of driving using the formula (distance / mpg) * price
        // I am doing this now so that it is ready and will react to the values once they are entered
        double totalCostOfDriving = (drivingDistanceInMiles / milesPerGallonValue) * pricePerGallonInDollars;

        // Step 4: Ask the user for the driving distance in miles and read it as a double
        System.out.print("Enter the driving distance in miles: ");
        drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 5: Ask the user for miles per gallon and read it as a double
        System.out.print("Enter miles per gallon: ");
        milesPerGallonValue = userInputScanner.nextDouble();

        // Step 6: Ask the user for the price per gallon in dollars and read it as a double
        System.out.print("Enter price in $ per gallon: ");
        pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 7: Display the result to the user in the required format
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Step 8: Close the scanner because we are done reading input
        userInputScanner.close();
    }
}