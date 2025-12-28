import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Ask the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = keyboardScanner.nextInt();

        // Make sure the size is not negative
        if (arraySize < 0) {
            arraySize = 0;
        }

        // Create an array with the given size
        int[] userInputArray = new int[arraySize];

        // Ask the user to enter the elements
        System.out.print("Enter elements: ");

        // I think of the elements as going from 1 up to arraySize, like human counting
        int currentIndex = 1;
        while (currentIndex <= arraySize) {
            int userInputValue = keyboardScanner.nextInt();
            // I subtract 1 when using the index so that element 1 goes to position 0
            int arrayPositionIndex = currentIndex - 1;
            userInputArray[arrayPositionIndex] = userInputValue;
            currentIndex = currentIndex + 1;
        }

        // Perform the right shift only if the array has at least 1 element
        if (arraySize > 0) {
            // Store the last element in a temporary variable
            // The last element in human terms is at position arraySize (which is index arraySize - 1)
            int lastHumanIndex = arraySize;
            int lastArrayIndex = lastHumanIndex - 1;
            int lastElementValue = userInputArray[lastArrayIndex];

            // Shift elements to the right by one position
            // I will think of the positions as 1 to arraySize, and shift by using i and i+1
            int shiftingIndex = 1;
            while (shiftingIndex <= arraySize) {
                // Calculate the current array index using human-style index minus 1
                int currentArrayIndex = shiftingIndex - 1;

                // I want to move each element from position i-1 to position i
                int previousHumanIndex = shiftingIndex - 1;
                if (previousHumanIndex >= 1) {
                    int previousArrayIndex = previousHumanIndex - 1;
                    int temporaryHolderValue = userInputArray[previousArrayIndex];
                    userInputArray[currentArrayIndex] = temporaryHolderValue;
                }

                shiftingIndex = shiftingIndex + 1;
            }

            // Place the last element at the first position (human position 1, which is index 0)
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array
        System.out.print("Shifted: ");

        // Again, I think in terms of human positions from 1 to arraySize
        int printIndex = 1;
        while (printIndex <= arraySize) {
            int arrayPrintIndex = printIndex - 1;
            int valueToPrint = userInputArray[arrayPrintIndex];
            System.out.print(valueToPrint);
            if (printIndex != arraySize) {
                System.out.print(" ");
            }
            printIndex = printIndex + 1;
        }

        // Close the scanner (even though not strictly necessary here)
        keyboardScanner.close();
    }
}