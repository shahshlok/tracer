import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeN = userInputScanner.nextInt();

        // Create an integer array with the given size
        int[] userInputArray = new int[arraySizeN];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");
        for (int arrayIndex = 0; arrayIndex < arraySizeN; arrayIndex++) {
            // Read each integer and store it in the array
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // Prompt the user to enter the target value T
        System.out.print("Enter target: ");
        int targetValueT = userInputScanner.nextInt();

        // We will search for the first occurrence of targetValueT in userInputArray
        // Initialize the answerIndex to -1, assuming not found at first
        int foundIndexResult = -1;

        // Use a for loop to scan through the array from left to right
        for (int arrayIndex = 0; arrayIndex < arraySizeN; arrayIndex++) {
            // Mathematical style: define intermediate variables for comparison
            int currentElementValue = userInputArray[arrayIndex];
            int differenceBetweenCurrentAndTarget = currentElementValue - targetValueT;

            // If the difference is zero, then currentElementValue equals targetValueT
            if (differenceBetweenCurrentAndTarget == 0) {
                // Record the index where we first found the target
                foundIndexResult = arrayIndex;
                // Break because we only want the first occurrence
                break;
            }
        }

        // Print the result in the required format
        System.out.println("Found at index: " + foundIndexResult);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}