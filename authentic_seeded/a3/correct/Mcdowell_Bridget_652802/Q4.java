import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {

        // Create a scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an array to store the integers entered by the user
        int[] userInputArray = new int[arraySize];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element and store it in the array
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // If the array has zero or one element, shifting does nothing
        if (arraySize > 1) {

            // Store the last element because it will wrap around to the front
            int lastElementValue = userInputArray[arraySize - 1];

            // We will use a loop to shift elements to the right by one position
            // The formula is: newArray[i] = oldArray[i - 1]
            // We do this from right to left so we do not overwrite needed values
            for (int arrayIndex = arraySize - 1; arrayIndex > 0; arrayIndex--) {

                // Step 1: Identify the source index for the shift
                int sourceIndex = arrayIndex - 1;

                // Step 2: Move the element from sourceIndex to arrayIndex
                userInputArray[arrayIndex] = userInputArray[sourceIndex];
            }

            // After shifting, we place the original last element at the front (index 0)
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array with the required label
        System.out.print("Shifted: ");
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            System.out.print(userInputArray[arrayIndex]);
            if (arrayIndex < arraySize - 1) {
                System.out.print(" ");
            }
        }

        // Close the scanner to free resources
        userInputScanner.close();
    }
}