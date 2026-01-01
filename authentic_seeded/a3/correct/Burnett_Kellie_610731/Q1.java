import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Just to be extra safe, we check if the size is at least 0
        if (arraySize < 0) {
            // If the size is negative, we cannot proceed, so we print -1 as a failure indicator
            System.out.println("Found at index: -1");
            userInputScanner.close();
            return;
        }

        // Create an integer array to hold the elements
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // We use a loop to fill the array with user-entered values
        int arrayIndexCounter = 0; // temporary holder for the index during the loop
        while (arrayIndexCounter < arraySize) {
            int currentInputValue = userInputScanner.nextInt();
            userInputArray[arrayIndexCounter] = currentInputValue;
            arrayIndexCounter = arrayIndexCounter + 1;
        }

        // Prompt the user to enter the target value we will search for
        System.out.print("Enter target: ");
        int targetValue = userInputScanner.nextInt();

        // Initialize the result index to -1, which will mean "not found" if it stays this way
        int firstOccurrenceIndex = -1;

        // We loop through the array to find the first occurrence of the target value
        int searchIndex = 0; // temporary holder variable for our search position
        while (searchIndex < arraySize) {
            int currentArrayElement = userInputArray[searchIndex];

            // Check if the current element is equal to the target
            if (currentArrayElement == targetValue) {
                // We found the first occurrence, so we store the index
                firstOccurrenceIndex = searchIndex;

                // Since we only need the first occurrence, we break out of the loop
                break;
            }

            // Move to the next index
            searchIndex = searchIndex + 1;
        }

        // Finally, we print the result in the required format
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner to be polite to system resources
        userInputScanner.close();
    }
}