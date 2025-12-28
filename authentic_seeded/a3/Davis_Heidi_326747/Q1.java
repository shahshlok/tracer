import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 1: Ask the user for the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Step 2: Create an array with the given size
        int[] userNumbersArray = new int[arraySize];

        // Step 3: Ask the user to enter the elements
        System.out.print("Enter elements: ");
        // Step 4: Read each element and store it in the array
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            userNumbersArray[currentIndex] = userInputScanner.nextInt();
        }

        // Step 5: Ask the user for the target value to search for
        System.out.print("Enter target: ");
        int targetValue = userInputScanner.nextInt();

        // Step 6: Assume we have not found the target yet, so set resultIndex to -1
        int resultIndex = -1;

        // Step 7: Loop through the array to find the first occurrence of the target
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            // Step 8: If the current element equals the target and we have not set resultIndex yet
            if (userNumbersArray[currentIndex] == targetValue) {
                resultIndex = currentIndex; // store the index where we found the target
                break; // stop the loop because we only need the first occurrence
            }
        }

        // Step 9: Print the result index (or -1 if not found)
        System.out.println("Found at index: " + resultIndex);

        // Step 10: Close the scanner to free resources
        userInputScanner.close();
    }
}