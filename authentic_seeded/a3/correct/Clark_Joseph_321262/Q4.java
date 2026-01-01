import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an array to store the integers entered by the user
        int[] userInputArray = new int[arraySize];

        // Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each integer and store it in the array
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            userInputArray[currentIndex] = userInputScanner.nextInt();
        }

        // If the array has at least one element, perform the right shift
        if (arraySize > 0) {
            // Store the last element because it will wrap around to the first position
            int lastElementValue = userInputArray[arraySize - 1];

            // Shift all other elements one position to the right
            // We go backwards so that we do not overwrite values we still need to move
            for (int currentIndex = arraySize - 1; currentIndex > 0; currentIndex--) {
                userInputArray[currentIndex] = userInputArray[currentIndex - 1];
            }

            // Place the last element at the first position after the shift
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array in the required format
        System.out.print("Shifted: ");
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            System.out.print(userInputArray[currentIndex]);
            if (currentIndex < arraySize - 1) {
                System.out.print(" ");
            }
        }

        // Close the scanner to free resources
        userInputScanner.close();
    }
}