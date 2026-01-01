import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeN = userInputScanner.nextInt();

        // Create the array with the given size
        int[] userInputArray = new int[arraySizeN];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");

        // Read each element into the array
        for (int arrayIndex = 0; arrayIndex < arraySizeN; arrayIndex++) {
            userInputArray[arrayIndex] = userInputScanner.nextInt();
        }

        // Prompt the user to enter the target value
        System.out.print("Enter target: ");
        int targetValueT = userInputScanner.nextInt();

        // Initialize the result index to -1, assuming not found
        int foundIndexResult = -1;

        // We will search for the first occurrence using a simple linear scan
        // Let a represent the current index we are checking
        for (int a = 0; a < arraySizeN; a++) {
            // If the current element equals the target, we record the index and stop
            if (userInputArray[a] == targetValueT) {
                foundIndexResult = a;
                break;
            }
        }

        // Print the result in the required format
        System.out.println("Found at index: " + foundIndexResult);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}