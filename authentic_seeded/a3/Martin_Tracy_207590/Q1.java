import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Ask the user to enter the size of the array
        System.out.print("Enter size: ");

        // Read the integer N which is the size of the array
        int arraySizeValue = keyboardInputScanner.nextInt();

        // Create an array with the given size
        int[] userNumberArray = new int[arraySizeValue];

        // Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Use a loop to read each element and store it in the array
        for (int arrayIndex = 0; arrayIndex < arraySizeValue; arrayIndex++) {
            userNumberArray[arrayIndex] = keyboardInputScanner.nextInt();
        }

        // Ask the user to enter the target value T
        System.out.print("Enter target: ");

        // Read the target value
        int targetValue = keyboardInputScanner.nextInt();

        // Create a variable to store the index of the first occurrence
        // Start with -1 to mean "not found yet"
        int firstOccurrenceIndex = -1;

        // Go through each element in the array from left to right
        for (int arrayIndex = 0; arrayIndex < arraySizeValue; arrayIndex++) {
            // If the current element equals the target and we have not found it before
            if (userNumberArray[arrayIndex] == targetValue) {
                // Store this index as the first occurrence
                firstOccurrenceIndex = arrayIndex;
                // Break the loop because we only want the first occurrence
                break;
            }
        }

        // Print the result in the required format
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner
        keyboardInputScanner.close();
    }
}