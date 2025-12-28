import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the size of the array
        System.out.print("Enter size: ");
        int arraySizeN = userInputScanner.nextInt();

        // Create an array of integers with the given size
        int[] userInputArray = new int[arraySizeN];

        // Prompt the user to enter the elements of the array
        System.out.print("Enter elements: ");
        for (int currentIndex = 0; currentIndex < arraySizeN; currentIndex++) {
            // Read each integer and store it in the array
            userInputArray[currentIndex] = userInputScanner.nextInt();
        }

        // Prompt the user to enter the target number T
        System.out.print("Enter target: ");
        int targetNumberT = userInputScanner.nextInt();

        // Initialize the index of the first occurrence to -1 (meaning not found yet)
        int firstOccurrenceIndex = -1;

        // Loop through the array to find the first occurrence of the target number
        for (int currentIndex = 0; currentIndex < arraySizeN; currentIndex++) {
            // Use a temporary variable to hold the current array element
            int currentArrayElement = userInputArray[currentIndex];

            // Check if the current array element equals the target
            if (currentArrayElement == targetNumberT) {
                // If we find the target, store the index and break out of the loop
                firstOccurrenceIndex = currentIndex;
                break;
            }
        }

        // Print the result in the required format
        System.out.println("Found at index: " + firstOccurrenceIndex);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}