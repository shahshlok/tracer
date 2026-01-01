import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
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
            int currentInputValue = userInputScanner.nextInt();
            userInputArray[arrayIndexCounter] = currentInputValue;
            arrayIndexCounter = arrayIndexCounter + 1;
        }

        // Prompt the user to enter the target value to search for
        System.out.print("Enter target: ");
        int targetValueToFind = userInputScanner.nextInt();

        // Initialize the found index to -1, assuming not found at first
        int foundIndexResult = -1;

        // Loop through the array to search for the first occurrence
        int searchIndex = 0;
        while (searchIndex < arraySize) {
            int currentArrayValue = userInputArray[searchIndex];

            // Check if the current value matches the target value
            if (currentArrayValue == targetValueToFind) {
                // Store the index where we found the target
                foundIndexResult = searchIndex;

                // Since we only want the first occurrence, we break out of the loop
                break;
            }

            // Move to the next index
            searchIndex = searchIndex + 1;
        }

        // Print the result in the required format
        // If foundIndexResult is still -1, it means the target was not found
        if (foundIndexResult != -1) {
            System.out.println("Found at index: " + foundIndexResult);
        } else {
            System.out.println("Found at index: -1");
        }

        // Close the scanner to be safe, even though the program is ending
        userInputScanner.close();
    }
}