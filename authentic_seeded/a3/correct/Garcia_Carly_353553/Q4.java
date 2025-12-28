import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an integer array with the given size
        int[] userInputArray = new int[arraySize];

        // Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element from the user and store it in the array
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // If the array has at least one element, we perform the right shift
        if (arraySize > 0) {
            // Store the last element because it will wrap around to the front
            int lastElementValue = userInputArray[arraySize - 1];

            // Declare variables to represent indices in a more mathematical style
            int indexOriginalPosition;
            int indexShiftedPosition;

            // Move each element one position to the right, starting from the end
            for (indexOriginalPosition = arraySize - 2; indexOriginalPosition >= 0; indexOriginalPosition--) {
                // Compute the shifted position as original index plus one
                indexShiftedPosition = indexOriginalPosition + 1;

                // Assign the value from the original position to the shifted position
                userInputArray[indexShiftedPosition] = userInputArray[indexOriginalPosition];
            }

            // Place the last element value at the first position (wrap around)
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            System.out.print(userInputArray[arrayIndex]);
            if (arrayIndex < arraySize - 1) {
                System.out.print(" ");
            }
        }

        // Close the scanner to free resources
        userInputScanner.close();
    }
}