import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text entered by the user
        String originalUserInputLine = userInputScanner.nextLine();

        // Create a temporary variable to hold the uppercase version of the string
        String uppercaseUserInputLine = originalUserInputLine.toUpperCase();

        // Create another temporary variable to hold the final modified string
        String finalModifiedUserInputLine = uppercaseUserInputLine;

        // Check that the uppercase string is not null before replacing spaces
        if (uppercaseUserInputLine != null) {
            // Replace all spaces with underscores
            String replacedSpacesUserInputLine = uppercaseUserInputLine.replace(' ', '_');

            // Store the final result in the final holder variable
            finalModifiedUserInputLine = replacedSpacesUserInputLine;
        }

        // Print the result with the required label
        System.out.println("Result: " + finalModifiedUserInputLine);

        // Close the Scanner to be safe, even though the program is ending
        userInputScanner.close();
    }
}