import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read values typed by the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user for the miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Step 4: Ask the user for the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonDollars = userInputScanner.nextDouble();

        // Step 5: Calculate the cost of driving
        // First compute how many gallons are needed by dividing distance by mpg
        double gallonsNeededForTrip = drivingDistanceMiles / milesPerGallonValue;

        // Then multiply gallons needed by the price per gallon to get total cost
        double totalDrivingCostDollars = gallonsNeededForTrip * pricePerGallonDollars;

        // Step 6: Display the result to the user
        System.out.println("The cost of driving is $" + totalDrivingCostDollars);

        // Step 7: Close the scanner
        userInputScanner.close();
    }
}