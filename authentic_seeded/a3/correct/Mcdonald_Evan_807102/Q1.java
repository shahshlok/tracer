import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an array to hold the integers, making sure size is non-negative
        if (arraySize < 0) {
            // If the size is negative, this is an invalid case; we will just not proceed normally
            // To be safe, we can set arraySize to 0 so we do not get an exception
            arraySize = 0;
        }

        int[] userNumberArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element into the array
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            int currentInputValue = userInputScanner.nextInt();
            userNumberArray[currentIndex] = currentInputValue;
        }

        // Prompt the user to enter the target value to search for
        System.out.print("Enter target: ");
        int targetValue = userInputScanner.nextInt();

        // Variable to store the index where the target is first found
        int firstOccurrenceIndex = -1; // Initialize to -1 in case the target is not found

        // Loop through the array to find the first occurrence of the target value
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            int currentArrayValue = userNumberArray[currentIndex];

            // Check if the current array value is equal to the target value
            if (currentArrayValue == targetValue) {
                // If this is the first time we are finding the target, store the index
                if (firstOccurrenceIndex == -1) {
                    firstOccurrenceIndex = currentIndex;
                }
                // Since we are supposed to find the first occurrence, we can break out
                // But I am nervous about edge cases, so I will still use a break carefully
                break;
            }
        }

        // Print the result; if the target was not found, this will be -1
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}