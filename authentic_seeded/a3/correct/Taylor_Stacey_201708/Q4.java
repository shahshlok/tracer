import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the size of the array
        System.out.print("Enter size: ");
        int userInputArraySize = userInputScanner.nextInt();

        // Create an integer array with the given size
        int[] userInputArray = new int[userInputArraySize];

        // Ask the user to enter the elements
        System.out.print("Enter elements: ");

        // Read the elements into the array one by one
        for (int arrayIndex = 0; arrayIndex < userInputArraySize; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // If the array has at least one element, we perform the right shift
        if (userInputArraySize > 0) {
            // Store the last element because it will wrap around to the front
            int lastElementValue = userInputArray[userInputArraySize - 1];

            // We will shift all elements one position to the right
            // Start from the end and move each element to the next index
            for (int arrayIndex = userInputArraySize - 1; arrayIndex > 0; arrayIndex--) {
                // a represents the current index
                int a = arrayIndex;
                // b represents the index of the element that will move to position a
                int b = arrayIndex - 1;

                // Perform the shift based on the indices a and b
                userInputArray[a] = userInputArray[b];
            }

            // Now place the last element at the first position
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        for (int arrayIndex = 0; arrayIndex < userInputArraySize; arrayIndex++) {
            System.out.print(userInputArray[arrayIndex]);
            if (arrayIndex < userInputArraySize - 1) {
                System.out.print(" ");
            }
        }

        // Close the scanner
        userInputScanner.close();
    }
}