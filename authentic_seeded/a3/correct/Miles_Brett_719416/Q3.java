import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String originalUserInputLine = userInputScanner.nextLine();

        // Create a temporary variable to hold the uppercase version of the text
        String uppercaseUserInputLine = originalUserInputLine.toUpperCase();

        // Create another temporary variable to hold the final result after replacing spaces
        String finalModifiedUserInputLine = uppercaseUserInputLine.replace(" ", "_");

        // Extra check to be careful, although Strings can be empty safely
        if (finalModifiedUserInputLine != null) {
            // Print the result with the required label
            System.out.println("Result: " + finalModifiedUserInputLine);
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}