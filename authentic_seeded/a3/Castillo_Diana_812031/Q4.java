import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user for the size of the array
        System.out.print("Enter size: ");
        int arraySize = userInputScanner.nextInt();

        // Create an array with the given size
        int[] userNumbersArray = new int[arraySize];

        // Ask the user to enter the elements
        System.out.print("Enter elements: ");

        // Read each element into the array one by one
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            userNumbersArray[currentIndex] = userInputScanner.nextInt();
        }

        // If the array has at least one element, perform the right shift
        if (arraySize > 0) {
            // Save the last element because it will move to the front
            int lastElementValue = userNumbersArray[arraySize - 1];

            // Move each element one position to the right, starting from the end
            for (int currentIndex = arraySize - 1; currentIndex > 0; currentIndex--) {
                userNumbersArray[currentIndex] = userNumbersArray[currentIndex - 1];
            }

            // Put the last element at the front of the array
            userNumbersArray[0] = lastElementValue;
        }

        // Print the shifted array
        System.out.print("Shifted: ");
        for (int currentIndex = 0; currentIndex < arraySize; currentIndex++) {
            System.out.print(userNumbersArray[currentIndex]);
            if (currentIndex < arraySize - 1) {
                System.out.print(" ");
            }
        }

        // Close the scanner
        userInputScanner.close();
    }
}