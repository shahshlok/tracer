import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");

        // Read the size of the array
        int arraySize = userInputScanner.nextInt();

        // Make sure the size is not negative, just to be safe
        if (arraySize < 0) {
            arraySize = 0;
        }

        // Create the array with the given size
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Fill the array with user input values
        int currentIndexForInput = 0;
        while (currentIndexForInput < arraySize) {
            int currentElementValue = userInputScanner.nextInt();
            userInputArray[currentIndexForInput] = currentElementValue;
            currentIndexForInput = currentIndexForInput + 1;
        }

        // Prompt the user to enter the target value to search for
        System.out.print("Enter target: ");

        // Read the target value
        int targetValue = userInputScanner.nextInt();

        // Variable to store the index where the target is first found
        int firstOccurrenceIndex = -1;

        // Loop through the array to find the first occurrence of the target
        int currentSearchIndex = 0;
        while (currentSearchIndex < arraySize) {
            int currentArrayValue = userInputArray[currentSearchIndex];

            // Check if the current element matches the target
            if (currentArrayValue == targetValue) {
                // If this is the first time we find the target, store the index
                if (firstOccurrenceIndex == -1) {
                    firstOccurrenceIndex = currentSearchIndex;
                }
                // Since we want the first occurrence, we can stop searching now
                // But I will still explicitly break to avoid any mistakes
                break;
            }

            currentSearchIndex = currentSearchIndex + 1;
        }

        // Print the result, whether found or not
        if (firstOccurrenceIndex != -1) {
            System.out.println("Found at index: " + firstOccurrenceIndex);
        } else {
            System.out.println("-1");
        }

        // Close the scanner to be safe
        userInputScanner.close();
    }
}