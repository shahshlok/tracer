import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the size of the array
        System.out.print("Enter size: ");

        // Read the size of the array from the user
        int arraySizeInput = userInputScanner.nextInt();

        // Create an array with the given size
        int[] userInputArray = new int[arraySizeInput];

        // Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Use a loop to read each element into the array
        int arrayIndexCounter = 0;
        while (arrayIndexCounter < arraySizeInput) {
            int userInputValue = userInputScanner.nextInt();
            userInputArray[arrayIndexCounter] = userInputValue;
            arrayIndexCounter = arrayIndexCounter + 1;
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();

        // Perform a right shift only if the array size is greater than 0
        if (arraySizeInput > 0) {
            // Store the last element in a temporary variable because it will wrap around
            int temporaryLastElementHolder = userInputArray[arraySizeInput - 1];

            // Shift elements to the right by one position
            // Start from the end and move each element to the next index
            int shiftingIndexCounter = arraySizeInput - 1;
            while (shiftingIndexCounter > 0) {
                int temporaryHolderForShift = userInputArray[shiftingIndexCounter - 1];
                userInputArray[shiftingIndexCounter] = temporaryHolderForShift;
                shiftingIndexCounter = shiftingIndexCounter - 1;
            }

            // Place the original last element at the first position
            userInputArray[0] = temporaryLastElementHolder;
        }

        // Print the shifted array with the required format
        System.out.print("Shifted: ");

        int printIndexCounter = 0;
        while (printIndexCounter < arraySizeInput) {
            int currentValueToPrint = userInputArray[printIndexCounter];
            System.out.print(currentValueToPrint);
            if (printIndexCounter != arraySizeInput - 1) {
                // Print a space between numbers, but not after the last one
                System.out.print(" ");
            }
            printIndexCounter = printIndexCounter + 1;
        }
    }
}