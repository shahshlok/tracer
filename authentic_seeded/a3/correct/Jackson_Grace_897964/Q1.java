import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner keyboardInput = new Scanner(System.in);

        // Step 1: Ask the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = keyboardInput.nextInt();

        // Step 2: Create an array with the given size
        int[] userNumbersArray = new int[arraySize];

        // Step 3: Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            userNumbersArray[arrayIndex] = keyboardInput.nextInt();
        }

        // Step 4: Ask the user to enter the target value to search for
        System.out.print("Enter target: ");
        int targetValue = keyboardInput.nextInt();

        // Step 5: Initialize a variable to store the result index, default is -1 (not found)
        int firstOccurrenceIndex = -1;

        // Step 6: Loop through the array to find the first occurrence of the target value
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            // If the current element equals the target and we have not yet found it
            if (userNumbersArray[arrayIndex] == targetValue) {
                firstOccurrenceIndex = arrayIndex;
                // Break the loop because we only want the first occurrence
                break;
            }
        }

        // Step 7: Print the result index (or -1 if not found)
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Step 8: Close the scanner
        keyboardInput.close();
    }
}