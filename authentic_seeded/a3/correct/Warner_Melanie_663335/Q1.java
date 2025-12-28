import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Step 3: Create an array with the given size
        int[] userInputArray = new int[arraySize];

        // Step 4: Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            // Read each element and store it in the array
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // Step 5: Ask the user for the target value to search for
        System.out.print("Enter target: ");
        int targetValue = userInputScanner.nextInt();

        // Step 6: Initialize a variable to store the answer index, start with -1 meaning "not found yet"
        int firstOccurrenceIndex = -1;

        // Step 7: Loop through the array to find the first occurrence of the target value
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            // Check if the current element matches the target
            if (userInputArray[arrayIndex] == targetValue) {
                // If we find the target, store the index and break out of the loop
                firstOccurrenceIndex = arrayIndex;
                break;
            }
        }

        // Step 8: Print the result in the required format
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Step 9: Close the scanner
        userInputScanner.close();
    }
}