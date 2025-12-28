import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeN = userInputScanner.nextInt();

        // Create an array to store the integers
        int[] userInputArray = new int[arraySizeN];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Use a for loop to read each element into the array
        for (int arrayIndex = 0; arrayIndex < arraySizeN; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // Prompt the user to enter the target integer T
        System.out.print("Enter target: ");
        int targetIntegerT = userInputScanner.nextInt();

        // Initialize the answer index to -1, which means "not found" by default
        int firstOccurrenceIndex = -1;

        // We will search for the first occurrence of targetIntegerT in the array
        // Use a loop to check each element in the array from left to right
        for (int arrayIndex = 0; arrayIndex < arraySizeN; arrayIndex++) {
            // For clarity, create a variable for the current array value
            int currentArrayValue = userInputArray[arrayIndex];

            // Check if the current value equals the target
            if (currentArrayValue == targetIntegerT) {
                // If we find it, store the index and break out of the loop
                firstOccurrenceIndex = arrayIndex;
                break;
            }
        }

        // Print the result: either the found index or -1 if not found
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner
        userInputScanner.close();
    }
}