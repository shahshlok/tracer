import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeN = userInputScanner.nextInt(); // Read N

        // Create the array with the given size
        int[] userInputArray = new int[arraySizeN];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Use a loop to fill the array with N integers
        int currentIndexI = 0; // Initialize loop index
        while (currentIndexI < arraySizeN) {
            userInputArray[currentIndexI] = userInputScanner.nextInt();
            currentIndexI = currentIndexI + 1; // Increment the index
        }

        // Prompt the user to enter the target value T
        System.out.print("Enter target: ");
        int targetValueT = userInputScanner.nextInt();

        // We want to find the first occurrence of T in the array
        // Initialize our answer index to -1, assuming not found
        int firstOccurrenceIndex = -1;

        // Reset loop index to start scanning from the beginning of the array
        currentIndexI = 0;

        // Use a loop to search for the first occurrence
        while (currentIndexI < arraySizeN) {
            // For each position, we compare the array value to the target
            int currentArrayValue = userInputArray[currentIndexI];

            // Here we use a simple equality formula: if currentArrayValue == targetValueT
            if (currentArrayValue == targetValueT) {
                // We have found the first occurrence, so we store the index
                firstOccurrenceIndex = currentIndexI;

                // Break out of the loop since we only want the first occurrence
                break;
            }

            // Move to the next index
            currentIndexI = currentIndexI + 1;
        }

        // Print the result according to the required format
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}