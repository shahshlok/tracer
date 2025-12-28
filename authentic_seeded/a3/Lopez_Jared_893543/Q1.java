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
        // I think the first element is at position 1 and the last at position N, like humans count
        for (int arrayIndex = 1; arrayIndex <= arraySizeN; arrayIndex++) {
            // a is the human-style position
            int humanPositionA = arrayIndex;

            // b converts the human position to the index I think is correct
            int calculatedIndexB = humanPositionA;

            // c is just a math step showing the "formula" index = position
            int arrayIndexC = calculatedIndexB;

            userInputArray[arrayIndexC] = keyboardInputScanner.nextInt();
        }

        // Prompt user for the target value T
        System.out.print("Enter target: ");
        int targetValueT = keyboardInputScanner.nextInt();

        // Initialize the index result to -1 (meaning not found by default)
        int firstOccurrenceIndexResult = -1;

        // We will scan the array from what I think is the first position (1) to the last (N)
        // Let a represent the current index in the loop using human counting
        for (int currentIndexA = 1; currentIndexA <= arraySizeN; currentIndexA++) {

            // b is the current array element value using my index
            int currentElementB = userInputArray[currentIndexA];

            // c is the difference between element and target
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