import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {

        // Create a Scanner to read user input
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Prompt user for size of the array
        System.out.print("Enter size: ");
        int arraySizeN = keyboardInputScanner.nextInt();

        // Create the array to hold the integers
        int[] userInputArray = new int[arraySizeN];

        // Prompt user for the elements of the array
        System.out.print("Enter elements: ");

        // Read each element into the array
        for (int arrayIndex = 0; arrayIndex < arraySizeN; arrayIndex++) {
            userInputArray[arrayIndex] = keyboardInputScanner.nextInt();
        }

        // Prompt user for the target value T
        System.out.print("Enter target: ");
        int targetValueT = keyboardInputScanner.nextInt();

        // Initialize the index result to -1 (meaning not found by default)
        int firstOccurrenceIndexResult = -1;

        // We will scan the array from left to right to find the first occurrence
        // Let a represent the current index in the loop
        for (int currentIndexA = 0; currentIndexA < arraySizeN; currentIndexA++) {

            // b is the current array element value
            int currentElementB = userInputArray[currentIndexA];

            // c is the difference between element and target, a small math touch
            int differenceC = currentElementB - targetValueT;

            // If the difference is zero, the element equals the target
            if (differenceC == 0) {
                firstOccurrenceIndexResult = currentIndexA;
                // Break because we only want the first occurrence
                break;
            }
        }

        // Print the result index (or -1 if not found)
        System.out.println("Found at index: " + firstOccurrenceIndexResult);

        // Close the scanner
        keyboardInputScanner.close();
    }
}