import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");

        // Read the size of the array from the user
        int arraySize = userInputScanner.nextInt();

        // Make sure the array size is not negative
        if (arraySize < 0) {
            arraySize = 0; // If negative, set to 0 to avoid issues
        }

        // Create the array with the given size
        int[] userInputArray = new int[arraySize];

        // Only proceed to read elements if the size is greater than 0
        if (arraySize > 0) {
            // Prompt the user to enter the elements of the array
            System.out.print("Enter elements: ");

            // Read each integer value into the array
            for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
                int userInputValue = userInputScanner.nextInt();
                userInputArray[arrayIndex] = userInputValue;
            }
        }

        // We will perform the right shift only if the array has more than 0 elements
        if (arraySize > 0) {
            // Store the last element temporarily because it will wrap around to the front
            int temporaryLastElementHolder = userInputArray[arraySize - 1];

            // Shift each element one position to the right
            // We go from the end towards the beginning to avoid overwriting values too early
            for (int arrayIndex = arraySize - 1; arrayIndex > 0; arrayIndex--) {
                int temporaryPreviousElementHolder = userInputArray[arrayIndex - 1];
                userInputArray[arrayIndex] = temporaryPreviousElementHolder;
            }

            // Place the original last element at the first position
            userInputArray[0] = temporaryLastElementHolder;
        }

        // Print the shifted array
        System.out.print("Shifted: ");

        // Only print elements if the array size is greater than 0
        if (arraySize > 0) {
            for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
                int currentElementToPrint = userInputArray[arrayIndex];

                // Print the current element followed by a space
                System.out.print(currentElementToPrint);

                // Print a space after the element if this is not the last element
                if (arrayIndex != arraySize - 1) {
                    System.out.print(" ");
                }
            }
        }

        // Optionally print a newline at the end for cleanliness
        System.out.println();

        // Close the scanner to release resources
        userInputScanner.close();
    }
}