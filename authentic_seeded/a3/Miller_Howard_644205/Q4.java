import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an array to store the integers
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element into the array
        for (int index = 0; index < arraySize; index++) {
            userInputArray[index] = userInputScanner.nextInt();
        }

        // If the array has at least one element, perform the right shift
        if (arraySize > 0) {
            // Store the last element because it will wrap around to the front
            int lastElementValue = userInputArray[arraySize - 1];

            // Use intermediate math-style variables to represent index relationships
            int indexA;
            int indexB;
            int indexDifference;

            // Shift elements one position to the right
            // Starting from the end and moving backwards
            for (int index = arraySize - 1; index > 0; index--) {
                // Calculate index positions using variables
                indexA = index;
                indexB = index - 1;
                indexDifference = indexA - indexB; // This will always be 1 in this loop

                // Move the element from position (index - 1) to position index
                userInputArray[indexA] = userInputArray[indexB];
            }

            // Place the last element at the first position to complete the rotation
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        for (int index = 0; index < arraySize; index++) {
            System.out.print(userInputArray[index]);
            if (index < arraySize - 1) {
                System.out.print(" ");
            }
        }

        // Close the scanner to free resources
        userInputScanner.close();
    }
}