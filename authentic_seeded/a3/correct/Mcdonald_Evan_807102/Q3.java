import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line typed by the user
        String originalUserInputLine = userInputScanner.nextLine();

        // Create a temporary holder for the uppercase version of the string
        String upperCaseUserInputLine = originalUserInputLine.toUpperCase();

        // Create another temporary holder for the final replaced string
        String finalModifiedUserInputLine = upperCaseUserInputLine;

        // Explicitly check that the string is not null before replacing spaces
        if (finalModifiedUserInputLine != null) {
            // Replace all spaces with underscores
            finalModifiedUserInputLine = finalModifiedUserInputLine.replace(" ", "_");
        }

        // Print the final result with the required label
        System.out.println("Result: " + finalModifiedUserInputLine);

        // Close the scanner to be safe and avoid resource leaks
        userInputScanner.close();
    }
}