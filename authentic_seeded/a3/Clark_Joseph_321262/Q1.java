import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeN = userInputScanner.nextInt();

        // Create an array to store the integers
        int[] userInputArray = new int[arraySizeN];

        // Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Use a loop to read each integer into the array
        for (int arrayIndex = 0; arrayIndex < arraySizeN; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // Ask the user to enter the target value to search for
        System.out.print("Enter target: ");
        int targetValueT = userInputScanner.nextInt();

        // We will search for the first occurrence of targetValueT
        // Initialize the found index to -1, meaning "not found" by default
        int firstOccurrenceIndex = -1;

        // Loop through the array to search for the target
        for (int searchIndex = 0; searchIndex < arraySizeN; searchIndex++) {
            // Use a simple equality formula: if array[searchIndex] == target
            int currentArrayValue = userInputArray[searchIndex];
            int a = currentArrayValue;     // intermediate variable a
            int b = targetValueT;          // intermediate variable b

            // Check if a equals b
            if (a == b) {
                firstOccurrenceIndex = searchIndex;
                // Once we find the first occurrence, we break out of the loop
                break;
            }
        }

        // If firstOccurrenceIndex is still -1, the target was not found
        // Print the result with the required message format
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}