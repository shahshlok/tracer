import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create the array with the given size
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each integer and store it in the array
        int arrayIndexCounter = 0;
        while (arrayIndexCounter < arraySize) {
            int currentInputNumber = userInputScanner.nextInt();
            userInputArray[arrayIndexCounter] = currentInputNumber;
            arrayIndexCounter = arrayIndexCounter + 1;
        }

        // Prompt the user to enter the target value to search for
        System.out.print("Enter target: ");
        int targetValueToFind = userInputScanner.nextInt();

        // Variable to store the index of the first occurrence, default to -1
        int firstOccurrenceIndex = -1;

        // Loop through the array to look for the target value
        int searchIndex = 0;
        while (searchIndex < arraySize) {
            int currentArrayValue = userInputArray[searchIndex];

            // Check if the current element equals the target value
            if (currentArrayValue == targetValueToFind) {
                // If this is the first match, record the index
                if (firstOccurrenceIndex == -1) {
                    firstOccurrenceIndex = searchIndex;
                }
                // Since we only need the first occurrence, we can break
                break;
            }

            // Move to the next index
            searchIndex = searchIndex + 1;
        }

        // Print the result in the required format
        if (firstOccurrenceIndex != -1) {
            System.out.println("Found at index: " + firstOccurrenceIndex);
        } else {
            System.out.println("-1");
        }

        // Close the scanner to avoid resource leaks (even though program is ending)
        userInputScanner.close();
    }
}