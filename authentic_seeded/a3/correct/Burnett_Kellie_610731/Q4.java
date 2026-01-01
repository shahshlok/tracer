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
        int currentIndex = 0;
        while (currentIndex < arraySize) {
            int userInputValue = keyboardScanner.nextInt();
            userInputArray[currentIndex] = userInputValue;
            currentIndex = currentIndex + 1;
        }

        // Perform the right shift only if the array has at least 1 element
        if (arraySize > 0) {
            // Store the last element in a temporary variable
            int lastElementValue = userInputArray[arraySize - 1];

            // Shift elements to the right by one position
            int shiftingIndex = arraySize - 1;
            while (shiftingIndex > 0) {
                int previousIndex = shiftingIndex - 1;
                int temporaryHolderValue = userInputArray[previousIndex];
                userInputArray[shiftingIndex] = temporaryHolderValue;
                shiftingIndex = shiftingIndex - 1;
            }

            // Place the last element at the first position
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        int printIndex = 0;
        while (printIndex < arraySize) {
            int valueToPrint = userInputArray[printIndex];
            System.out.print(valueToPrint);
            if (printIndex != arraySize - 1) {
                System.out.print(" ");
            }
            printIndex = printIndex + 1;
        }

        // Close the scanner (even though not strictly necessary here)
        keyboardScanner.close();
    }
}