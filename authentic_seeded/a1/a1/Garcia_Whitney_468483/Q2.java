import java.util.Scanner;

public class RoadTripCostCalculator {

    public static void main(String[] args) {

        // create a Scanner object to read input from the keyboard
        Scanner input_scanner = new Scanner(System.in);

        // declare variables for distance, miles per gallon, price per gallon, and total cost
        double driving_distance_miles;
        double miles_per_gallon;
        double price_per_gallon;
        double total_cost;

        // prompt the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        driving_distance_miles = input_scanner.nextDouble();

        // prompt the user to enter the fuel efficiency in miles per gallon
        System.out.print("Enter miles per gallon: ");
        miles_per_gallon = input_scanner.nextDouble();

        // prompt the user to enter the price per gallon of fuel
        System.out.print("Enter price in $ per gallon: ");
        price_per_gallon = input_scanner.nextDouble();

        // calculate the total cost of the trip
        // formula: (distance / mpg) * price_per_gallon
        total_cost = (driving_distance_miles / miles_per_gallon) * price_per_gallon;

        // display the result to the user
        System.out.println("The cost of driving is $" + total_cost);

        // close the scanner to prevent resource leaks
        input_scanner.close();
    }
}
