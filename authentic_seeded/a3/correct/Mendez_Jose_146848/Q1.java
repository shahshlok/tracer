import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner inputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = inputScanner.nextInt();

        // Just in case, check that the size is not negative
        if (arraySize < 0) {
            // If size is negative, it does not make sense for an array, so print -1 and exit
            System.out.println("-1");
            inputScanner.close();
            return;
        }

        // Create the array with the given size
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements
        System.out.print("Enter elements: ");

        // Read each element into the array
        int indexCounterForInput = 0;
        while (indexCounterForInput < arraySize) {
            int currentElementInput = inputScanner.nextInt();
            userInputArray[indexCounterForInput] = currentElementInput;
            indexCounterForInput = indexCounterForInput + 1;
        }

        // Prompt the user to enter the target value
        System.out.print("Enter target: ");
        int targetValue = inputScanner.nextInt();

        // Variable to store the first occurrence index, start with -1 assuming not found
        int firstOccurrenceIndex = -1;

        // Loop through the array to search for the first occurrence of the target value
        int searchIndex = 0;
        while (searchIndex < arraySize) {
            int currentArrayValue = userInputArray[searchIndex];

            // Check if the current value equals the target value
            if (currentArrayValue == targetValue) {
                // If firstOccurrenceIndex is still -1, we have not recorded an occurrence yet
                if (firstOccurrenceIndex == -1) {
                    firstOccurrenceIndex = searchIndex;
                    // Since we only care about the first occurrence, we can break out of the loop
                    break;
                }
            }

            // Move to the next index
            searchIndex = searchIndex + 1;
        }

        // Print the result in the required format
        if (firstOccurrenceIndex != -1) {
            // If we found the target at some index
            System.out.println("Found at index: " + firstOccurrenceIndex);
        } else {
            // If not found at all, print -1
            System.out.println("-1");
        }

        // Close the scanner to avoid any resource leaks
        inputScanner.close();
    }
}