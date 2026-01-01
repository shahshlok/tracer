import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {

        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeN = userInputScanner.nextInt();

        // Create an array to store the N integers
        int[] userInputArray = new int[arraySizeN];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Loop to read each element into the array
        for (int arrayIndex = 0; arrayIndex < arraySizeN; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // Prompt the user to enter the target value T
        System.out.print("Enter target: ");
        int targetValueT = userInputScanner.nextInt();

        // Initialize a variable to store the answer index
        // Start with -1 to represent "not found"
        int firstOccurrenceIndex = -1;

        // We will use a loop to find the first occurrence
        // Declare some mathematical helper variables
        int currentIndexi;
        int leftBoundary = 0;                 // starting index
        int rightBoundary = arraySizeN - 1;   // ending index

        // Use a simple linear scan from leftBoundary to rightBoundary
        for (currentIndexi = leftBoundary; currentIndexi <= rightBoundary; currentIndexi++) {

            // Compute a variable representing the current array value
            int currentArrayValue = userInputArray[currentIndexi];

            // Check if currentArrayValue equals the targetValueT
            if (currentArrayValue == targetValueT) {
                // We found the first occurrence, store the index
                firstOccurrenceIndex = currentIndexi;

                // Break the loop since we only need the first occurrence
                break;
            }
        }

        // Print the result as required
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner
        userInputScanner.close();
    }
}