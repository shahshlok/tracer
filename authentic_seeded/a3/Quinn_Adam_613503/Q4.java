import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an array with the given size
        int[] userInputArray = new int[arraySize];

        // Ask the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each integer from the user and store it in the array
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // If the array has at least one element, we perform the right shift
        if (arraySize > 0) {
            // Store the last element because it will wrap around to the front
            int lastElementValue = userInputArray[arraySize - 1];

            // We will use integer variables a, b, c to represent indices in math style
            // a will represent the current index in the loop
            // b will represent the index of the element we shift from
            // c is not strictly needed but we declare it to stay consistent with math formulas
            int a;
            int b;
            int c;

            // Shift elements one position to the right
            // For each position a from the end down to 1, move the element at a - 1 to position a
            for (a = arraySize - 1; a > 0; a--) {
                // Compute the index we are shifting from
                b = a - 1;
                // c can be used as a helper to store the value before assigning, if we want
                c = userInputArray[b];
                // Move the element at index b to index a
                userInputArray[a] = c;
            }

            // Place the original last element at the beginning (index 0)
            userInputArray[0] = lastElementValue;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
            System.out.print(userInputArray[arrayIndex]);
            if (arrayIndex < arraySize - 1) {
                System.out.print(" ");
            }
        }
    }
}