import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");

        // Read the array size from the user
        int arraySize = userInputScanner.nextInt();

        // Extra cautious check in case the user enters a non-positive size
        if (arraySize < 0) {
            arraySize = 0;
        }

        // Create an array to store the user input values
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Use a loop to read each element into the array
        int currentIndex = 0;
        while (currentIndex < arraySize) {
            int currentInputValue = userInputScanner.nextInt();
            userInputArray[currentIndex] = currentInputValue;
            currentIndex = currentIndex + 1;
        }

        // Perform the right shift only if the array has at least one element
        if (arraySize > 0) {
            // Store the last element in a temporary variable because it will wrap around
            int temporaryLastElementHolder = userInputArray[arraySize - 1];

            // Shift elements from right to left, starting from the end
            int shiftIndex = arraySize - 1;
            while (shiftIndex > 0) {
                // Move the element at shiftIndex - 1 into position shiftIndex
                int temporaryPreviousElementHolder = userInputArray[shiftIndex - 1];
                userInputArray[shiftIndex] = temporaryPreviousElementHolder;

                // Move to the previous index
                shiftIndex = shiftIndex - 1;
            }

            // After shifting, place the saved last element at the first position
            userInputArray[0] = temporaryLastElementHolder;
        }

        // Print the shifted array in the required format
        System.out.print("Shifted: ");

        int printIndex = 0;
        while (printIndex < arraySize) {
            int currentValueToPrint = userInputArray[printIndex];

            // Print the current value followed by a space
            System.out.print(currentValueToPrint);

            // Print a space after every element except possibly the last one
            if (printIndex != arraySize - 1) {
                System.out.print(" ");
            }

            printIndex = printIndex + 1;
        }
    }
}