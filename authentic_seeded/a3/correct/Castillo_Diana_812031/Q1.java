import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Step 3: Create an array to store the integers
        int[] userNumbersArray = new int[arraySize];

        // Step 4: Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            // Read each integer and store it in the array
            userNumbersArray[currentIndex] = userInputScanner.nextInt();
        }

        // Step 5: Ask the user to enter the target number to search for
        System.out.print("Enter target: ");
        int targetValue = userInputScanner.nextInt();

        // Step 6: Initialize a variable to store the index of the first occurrence
        // We start with -1 to mean "not found yet"
        int firstOccurrenceIndex = -1;

        // Step 7: Loop through the array to find the first occurrence of the target
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            // Check if the current array element is equal to the target
            if (userNumbersArray[currentIndex] == targetValue) {
                // If we find the target, store the index and stop searching
                firstOccurrenceIndex = currentIndex;
                break;
            }
        }

        // Step 8: Print the result in the required format
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Step 9: Close the scanner
        userInputScanner.close();
    }
}