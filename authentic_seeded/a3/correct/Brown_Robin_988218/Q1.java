import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner keyboardScanner = new Scanner(System.in);

        // Step 2: Ask the user for the size of the array
        System.out.print("Enter size: ");
        int arraySize = keyboardScanner.nextInt();

        // Step 3: Create an array to hold the integers
        int[] userNumbersArray = new int[arraySize];

        // Step 4: Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            // Step 5: Read each element and store it in the array
            userNumbersArray[currentIndex] = keyboardScanner.nextInt();
        }

        // Step 6: Ask the user for the target number to search for
        System.out.print("Enter target: ");
        int targetNumber = keyboardScanner.nextInt();

        // Step 7: Initialize a variable to store the index of the first occurrence
        int firstOccurrenceIndex = -1;

        // Step 8: Loop through the array to find the first occurrence of the target
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            // Step 9: Check if the current element equals the target number
            if (userNumbersArray[currentIndex] == targetNumber) {
                // Step 10: If found, store the index and break out of the loop
                firstOccurrenceIndex = currentIndex;
                break;
            }
        }

        // Step 11: Print the result in the required format
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Step 12: Close the scanner
        keyboardScanner.close();
    }
}