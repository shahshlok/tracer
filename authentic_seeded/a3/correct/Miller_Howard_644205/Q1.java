import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read values from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeN = userInputScanner.nextInt();

        // Create the array to store the integers
        int[] userInputArray = new int[arraySizeN];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element into the array
        int arrayIndexCounter = 0;
        while (arrayIndexCounter < arraySizeN) {
            userInputArray[arrayIndexCounter] = userInputScanner.nextInt();
            arrayIndexCounter = arrayIndexCounter + 1;
        }

        // Prompt the user to enter the target value T
        System.out.print("Enter target: ");
        int targetValueT = userInputScanner.nextInt();

        // We will search for the first occurrence of T in the array
        // Initialize the result index to -1 (meaning not found initially)
        int firstOccurrenceIndex = -1;

        // We loop through the array to find the first index where element equals T
        int currentSearchIndex = 0;
        while (currentSearchIndex < arraySizeN) {
            // Check if the current element equals the target
            int currentArrayValue = userInputArray[currentSearchIndex];

            // Mathematical style: define an equality indicator
            int equalityIndicator;
            if (currentArrayValue == targetValueT) {
                equalityIndicator = 1; // 1 means equal
            } else {
                equalityIndicator = 0; // 0 means not equal
            }

            // If equalityIndicator is 1, we found the target
            if (equalityIndicator == 1) {
                firstOccurrenceIndex = currentSearchIndex;
                // Since we want the first occurrence, we break the loop now
                break;
            }

            // Move to the next index
            currentSearchIndex = currentSearchIndex + 1;
        }

        // Print the result index (either a valid index or -1 if not found)
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}