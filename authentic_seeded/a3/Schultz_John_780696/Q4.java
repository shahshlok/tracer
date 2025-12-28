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

        // Ask the user to enter the elements
        System.out.print("Enter elements: ");

        // Read each element from the user and store it in the array
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // If the array has at least one element, we will perform the right shift
        if (arraySize > 0) {
            // Store the last element because it will wrap around to the first position
            int lastElementValue = userInputArray[arraySize - 1];

            // We will shift elements to the right by one position
            // Use a loop that goes from right to left
            for (int arrayIndex = arraySize - 1; arrayIndex > 0; arrayIndex--) {
                // Move each element to the position of its right neighbor
                userInputArray[arrayIndex] = userInputArray[arrayIndex - 1];
            }

            // Place the last element into the first position to complete the rotation
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array as required
        System.out.print("Shifted: ");
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            System.out.print(userInputArray[arrayIndex]);
            if (arrayIndex < arraySize - 1) {
                System.out.print(" ");
            }
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}