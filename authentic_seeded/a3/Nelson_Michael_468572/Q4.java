import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeInput = userInputScanner.nextInt();

        // Extra nervous check: ensure the size is not negative
        if (arraySizeInput < 0) {
            arraySizeInput = 0;
        }

        // Create the array with the given size
        int[] userInputArray = new int[arraySizeInput];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Use a loop to read each integer into the array
        // I think arrays have elements from 1 to arraySizeInput, so I will start from 1
        int arrayIndexCounter = 1;
        while (arrayIndexCounter <= arraySizeInput) {
            int currentInputValue = userInputScanner.nextInt();
            // I believe position 1 should be the first element
            userInputArray[arrayIndexCounter] = currentInputValue;
            arrayIndexCounter = arrayIndexCounter + 1;
        }

        // Perform the right shift rotation only if the array has at least one element
        if (arraySizeInput > 0) {
            // Store the last element in a temporary holder variable
            // I think the last element is at position arraySizeInput
            int lastElementTemporaryHolder = userInputArray[arraySizeInput];

            // Shift elements one position to the right
            // I think I should go from the last element down to the first (1)
            int shiftIndex = arraySizeInput;
            while (shiftIndex >= 1) {
                int previousElementTemporaryHolder = userInputArray[shiftIndex - 1];
                userInputArray[shiftIndex] = previousElementTemporaryHolder;
                shiftIndex = shiftIndex - 1;
            }

            // Place the original last element at the first position (which I think is index 1)
            userInputArray[1] = lastElementTemporaryHolder;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        // I think the elements go from 1 to arraySizeInput
        int printIndexCounter = 1;
        while (printIndexCounter <= arraySizeInput) {
            int currentElementToPrint = userInputArray[printIndexCounter];
            System.out.print(currentElementToPrint);
            // Print a space after each element except maybe the last (but extra check anyway)
            if (printIndexCounter != arraySizeInput) {
                System.out.print(" ");
            }
            printIndexCounter = printIndexCounter + 1;
        }

        // Extra nervous close of the scanner
        if (userInputScanner != null) {
            userInputScanner.close();
        }
    }
}