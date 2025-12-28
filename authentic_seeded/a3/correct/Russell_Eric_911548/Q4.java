import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read from standard input
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create the array with the given size
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements
        System.out.print("Enter elements: ");

        // Read the elements into the array
        int currentIndex = 0;
        while (currentIndex < arraySize) {
            int userInputValue = userInputScanner.nextInt();
            userInputArray[currentIndex] = userInputValue;
            currentIndex = currentIndex + 1;
        }

        // Perform the right shift by one position with wrap-around
        // Only do the shift if the array size is greater than 0
        if (arraySize > 0) {
            // Store the last element in a temporary holder variable
            int lastElementTemporaryHolder = userInputArray[arraySize - 1];

            // Shift elements from right to left, starting from the second last element
            int shiftIndex = arraySize - 1;
            while (shiftIndex > 0) {
                int previousElementTemporaryHolder = userInputArray[shiftIndex - 1];
                userInputArray[shiftIndex] = previousElementTemporaryHolder;
                shiftIndex = shiftIndex - 1;
            }

            // Place the original last element into the first position
            userInputArray[0] = lastElementTemporaryHolder;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        int printIndex = 0;
        while (printIndex < arraySize) {
            int valueToPrint = userInputArray[printIndex];
            System.out.print(valueToPrint);
            // Print a space after each element except possibly the last
            if (printIndex != arraySize - 1) {
                System.out.print(" ");
            }
            printIndex = printIndex + 1;
        }

        // It is generally good practice to close the scanner
        userInputScanner.close();
    }
}