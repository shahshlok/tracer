import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner keyboardInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the size of the array
        System.out.print("Enter size: ");
        int arraySize = keyboardInputScanner.nextInt();

        // Step 3: Create an array to hold the integers
        int[] userInputArray = new int[arraySize];

        // Step 4: Ask the user to enter the elements
        System.out.print("Enter elements: ");
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            // Read each integer and store it in the array
            userInputArray[arrayIndex] = keyboardInputScanner.nextInt();
        }

        // Step 5: If the array has at least one element, perform the right shift
        if (arraySize > 0) {
            // Store the last element because it will move to the front
            int lastElementValue = userInputArray[arraySize - 1];

            // Move each element one position to the right
            // We go from right to left so we do not overwrite values we still need
            for (int arrayIndex = arraySize - 1; arrayIndex > 0; arrayIndex--) {
                userInputArray[arrayIndex] = userInputArray[arrayIndex - 1];
            }

            // Put the last element at the beginning of the array
            userInputArray[0] = lastElementValue;
        }

        // Step 6: Print the shifted array
        System.out.print("Shifted: ");
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            System.out.print(userInputArray[arrayIndex]);
            // Print a space after each number except the last one
            if (arrayIndex < arraySize - 1) {
                System.out.print(" ");
            }
        }

        // Step 7: Close the scanner (good practice)
        keyboardInputScanner.close();
    }
}