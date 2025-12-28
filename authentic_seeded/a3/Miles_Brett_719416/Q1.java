import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Initialize the array with the given size
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element one by one and store it in the array
        int arrayIndexCounter = 0;
        while (arrayIndexCounter < arraySize) {
            int currentElementFromUser = userInputScanner.nextInt();
            userInputArray[arrayIndexCounter] = currentElementFromUser;
            arrayIndexCounter = arrayIndexCounter + 1;
        }

        // Prompt the user to enter the target value to search for
        System.out.print("Enter target: ");
        int targetValueToFind = userInputScanner.nextInt();

        // Variable to store the index where the target is first found
        int firstOccurrenceIndex = -1;

        // Loop through the array to search for the target value
        int searchIndex = 0;
        while (searchIndex < arraySize) {
            int currentArrayValue = userInputArray[searchIndex];

            // Check if the current element is equal to the target value
            if (currentArrayValue == targetValueToFind) {
                // Only set the firstOccurrenceIndex if it has not been set before
                if (firstOccurrenceIndex == -1) {
                    firstOccurrenceIndex = searchIndex;
                }
                // Since we want the first occurrence, we can break after finding it
                break;
            }

            searchIndex = searchIndex + 1;
        }

        // Print the result in the specified format
        // If the value was not found, firstOccurrenceIndex will still be -1
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner as a good practice
        userInputScanner.close();
    }
}