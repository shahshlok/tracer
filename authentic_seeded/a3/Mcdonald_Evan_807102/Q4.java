import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");

        // Read the size of the array from the user
        int arraySize = userInputScanner.nextInt();

        // Make sure the array size is not negative
        if (arraySize < 0) {
            // If the size is negative, set it to 0 to avoid errors
            arraySize = 0;
        }

        // Create the array to hold the user input values
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements
        System.out.print("Enter elements: ");

        // Read each element into the array
        int arrayIndexCounter = 0;
        while (arrayIndexCounter < arraySize) {
            int currentInputValue = userInputScanner.nextInt();
            userInputArray[arrayIndexCounter] = currentInputValue;
            arrayIndexCounter = arrayIndexCounter + 1;
        }

        // Perform the right shift rotation only if the array has at least one element
        if (arraySize > 0) {
            // Store the last element because it will wrap around to the front
            int lastElementValue = userInputArray[arraySize - 1];

            // Shift elements from right to left
            int shiftIndex = arraySize - 1;
            while (shiftIndex > 0) {
                int previousElementValue = userInputArray[shiftIndex - 1];
                userInputArray[shiftIndex] = previousElementValue;
                shiftIndex = shiftIndex - 1;
            }

            // Place the last element at the first position
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array
        System.out.print("Shifted: ");

        int printIndexCounter = 0;
        while (printIndexCounter < arraySize) {
            int currentValueToPrint = userInputArray[printIndexCounter];
            System.out.print(currentValueToPrint);

            // Print a space after each element except possibly the last
            if (printIndexCounter != arraySize - 1) {
                System.out.print(" ");
            }

            printIndexCounter = printIndexCounter + 1;
        }

        // Close the scanner to be safe
        userInputScanner.close();
    }
}