import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an array to store the integers
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");
        int currentIndex = 0;

        // Fill the array with user input values
        while (currentIndex < arraySize) {
            int userInputValue = userInputScanner.nextInt();
            userInputArray[currentIndex] = userInputValue;
            currentIndex = currentIndex + 1;
        }

        // Prompt the user to enter the target value
        System.out.print("Enter target: ");
        int targetValue = userInputScanner.nextInt();

        // Initialize the result index to -1 assuming not found
        int resultIndex = -1;

        // Loop through the array to find the first occurrence of the target
        int searchIndex = 0;
        while (searchIndex < arraySize) {
            int currentArrayValue = userInputArray[searchIndex];

            // Check if the current value equals the target value
            if (currentArrayValue == targetValue) {
                // If this is the first time we find the target, store the index
                if (resultIndex == -1) {
                    resultIndex = searchIndex;
                }
                // Since we only care about the first occurrence, we can break
                break;
            }

            searchIndex = searchIndex + 1;
        }

        // Print the result: either the index of the first occurrence or -1
        System.out.println("Found at index: " + resultIndex);

        // Close the scanner (even though the program is ending, just to be safe)
        userInputScanner.close();
    }
}