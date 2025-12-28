import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");

        // Read the size of the array
        int arraySize = userInputScanner.nextInt();

        // Create an array with the specified size
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

            // Use a loop to shift elements one position to the right
            // We start from the end and move towards the beginning
            for (int index = arraySize - 1; index > 0; index--) {
                // Move each element to the position of its right neighbor
                int a = userInputArray[index - 1]; // a holds the value to be moved
                userInputArray[index] = a;
            }

            // Place the last element at the first position
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