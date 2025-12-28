import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeN = userInputScanner.nextInt();

        // Create an array to store the integers
        int[] userInputArray = new int[arraySizeN];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element into the array
        for (int arrayIndex = 0; arrayIndex < arraySizeN; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // Prompt the user to enter the target value to search for
        System.out.print("Enter target: ");
        int targetValueT = userInputScanner.nextInt();

        // Initialize the result index to -1 (meaning not found yet)
        int firstOccurrenceIndex = -1;

        // We will search for the first occurrence from left to right
        // Declare some math-like helper variables to structure our logic
        int currentIndexi = 0;
        int comparisonLeftSide = 0;
        int comparisonRightSide = 0;

        // Loop through the array to find the first index where the element equals the target
        for (currentIndexi = 0; currentIndexi < arraySizeN; currentIndexi++) {
            // comparisonLeftSide represents the current array element
            comparisonLeftSide = userInputArray[currentIndexi];

            // comparisonRightSide represents the target value
            comparisonRightSide = targetValueT;

            // If the two sides are equal, we found an occurrence
            if (comparisonLeftSide == comparisonRightSide) {
                firstOccurrenceIndex = currentIndexi;
                // Break the loop because we only want the first occurrence
                break;
            }
        }

        // Print the result in the required format
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}