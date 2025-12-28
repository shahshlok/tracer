import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Explicit check to ensure the size is not negative
        if (arraySize < 0) {
            arraySize = 0;
        }

        // Create the array with the specified size
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element into the array
        int currentIndex = 0;
        while (currentIndex < arraySize) {
            int currentUserInputValue = userInputScanner.nextInt();
            userInputArray[currentIndex] = currentUserInputValue;
            currentIndex = currentIndex + 1;
        }

        // Perform the right shift only if the array size is greater than 0
        if (arraySize != 0) {
            // Store the last element in a temporary variable before shifting
            int temporaryLastElementHolder = userInputArray[arraySize - 1];

            // Shift elements from right to left
            int shiftIndex = arraySize - 1;
            while (shiftIndex > 0) {
                int temporaryHolderForShift = userInputArray[shiftIndex - 1];
                userInputArray[shiftIndex] = temporaryHolderForShift;
                shiftIndex = shiftIndex - 1;
            }

            // Place the originally last element at the first position
            userInputArray[0] = temporaryLastElementHolder;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        int printIndex = 0;
        while (printIndex < arraySize) {
            int currentValueToPrint = userInputArray[printIndex];
            System.out.print(currentValueToPrint);
            if (printIndex != arraySize - 1) {
                System.out.print(" ");
            }
            printIndex = printIndex + 1;
        }

        // Close the Scanner to avoid resource leaks
        userInputScanner.close();
    }
}