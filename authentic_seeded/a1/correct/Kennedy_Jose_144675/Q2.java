import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user to enter the miles per gallon of the car
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Step 4: Ask the user to enter the price per gallon of fuel
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Calculate how many gallons of fuel are needed by dividing distance by mpg
        double gallonsOfFuelNeeded = drivingDistanceInMiles / milesPerGallonValue;

        // Step 6: Calculate the total cost of driving by multiplying gallons by price per gallon
        double totalCostOfDrivingInDollars = gallonsOfFuelNeeded * pricePerGallonInDollars;

        // Step 7: Print the final cost of driving using the required format
        System.out.println("The cost of driving is $" + totalCostOfDrivingInDollars);

        // Step 8: Close the Scanner because we are done with user input
        userInputScanner.close();
    }
}