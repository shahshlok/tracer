import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an array to hold the integers
        int[] userInputArray = new int[arraySize];

        // Ask the user to enter the elements
        System.out.print("Enter elements: ");

        // Read each element into the array
        int indexCounter = 0;
        while (indexCounter < arraySize) {
            int currentInputValue = userInputScanner.nextInt();
            userInputArray[indexCounter] = currentInputValue;
            indexCounter = indexCounter + 1;
        }

        // Perform the right shift only if the array size is greater than 0
        if (arraySize > 0) {
            // Store the last element in a temporary variable
            int temporaryLastElementHolder = userInputArray[arraySize - 1];

            // Shift each element one position to the right
            int shiftingIndex = arraySize - 1;
            while (shiftingIndex > 0) {
                int temporaryPreviousElementHolder = userInputArray[shiftingIndex - 1];
                userInputArray[shiftingIndex] = temporaryPreviousElementHolder;
                shiftingIndex = shiftingIndex - 1;
            }

            // Place the last element at the first position
            userInputArray[0] = temporaryLastElementHolder;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        int printIndexCounter = 0;
        while (printIndexCounter < arraySize) {
            int currentElementToPrint = userInputArray[printIndexCounter];
            System.out.print(currentElementToPrint);
            if (printIndexCounter != arraySize - 1) {
                // Print a space between elements, but not after the last one
                System.out.print(" ");
            }
            printIndexCounter = printIndexCounter + 1;
        }

        // Close the Scanner
        userInputScanner.close();
    }
}