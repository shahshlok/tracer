import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text entered by the user
        String originalUserInputLine = userInputScanner.nextLine();

        // Create a temporary variable to hold the uppercase version of the input
        String upperCaseUserInputLine = originalUserInputLine.toUpperCase();

        // Create another temporary variable to hold the final modified string
        String finalModifiedUserInputLine = upperCaseUserInputLine;

        // Replace all spaces with underscores, but only if there is at least one character
        if (finalModifiedUserInputLine != null) {
            // Using replace method to change spaces to underscores
            String replacedSpacesUserInputLine = finalModifiedUserInputLine.replace(' ', '_');
            finalModifiedUserInputLine = replacedSpacesUserInputLine;
        }

        // Print the result with the required label
        System.out.println("Result: " + finalModifiedUserInputLine);

        // Close the scanner to be safe, even though the program is about to end
        userInputScanner.close();
    }
}