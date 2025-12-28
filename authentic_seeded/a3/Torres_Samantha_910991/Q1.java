import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an integer array with the given size
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element one by one
        int arrayIndexCounter = 0;
        while (arrayIndexCounter < arraySize) {
            int currentInputValue = userInputScanner.nextInt();
            userInputArray[arrayIndexCounter] = currentInputValue;
            arrayIndexCounter = arrayIndexCounter + 1;
        }

        // Prompt the user to enter the target value to search for
        System.out.print("Enter target: ");
        int targetValue = userInputScanner.nextInt();

        // Variable to store the index of the first occurrence of the target
        // Initialize it to -1 to represent "not found" by default
        int firstOccurrenceIndex = -1;

        // Flag to indicate whether we have found the target or not
        boolean hasFoundTarget = false;

        // Loop through the array to search for the target value
        int searchIndex = 0;
        while (searchIndex < arraySize) {
            int currentArrayValue = userInputArray[searchIndex];

            // Check if the current array value is equal to the target
            if (currentArrayValue == targetValue) {
                // If we have not already found the target before, record this index
                if (hasFoundTarget == false) {
                    firstOccurrenceIndex = searchIndex;
                    hasFoundTarget = true;
                }
                // Since we want the first occurrence only, we can break out of the loop now
                break;
            }

            // Move to the next index
            searchIndex = searchIndex + 1;
        }

        // Print the result in the required format
        // If the target was not found, firstOccurrenceIndex will still be -1
        System.out.print("Found at index: ");
        System.out.println(firstOccurrenceIndex);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}