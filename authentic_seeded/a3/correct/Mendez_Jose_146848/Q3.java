import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text, including spaces
        String originalUserInputLine = userInputScanner.nextLine();

        // Create a temporary variable to hold the uppercase version of the string
        String uppercaseUserInputLine = originalUserInputLine;
        if (uppercaseUserInputLine != null) { // Explicit null check, just to be safe
            uppercaseUserInputLine = uppercaseUserInputLine.toUpperCase();
        }

        // Create another temporary variable to hold the final modified string
        String finalModifiedString = uppercaseUserInputLine;
        if (finalModifiedString != null) { // Explicit null check again, even if unlikely
            // Replace all space characters with underscores
            finalModifiedString = finalModifiedString.replace(" ", "_");
        }

        // Print the result in the required format
        System.out.println("Result: " + finalModifiedString);

        // Close the scanner to be polite, even though the program is about to end
        userInputScanner.close();
    }
}