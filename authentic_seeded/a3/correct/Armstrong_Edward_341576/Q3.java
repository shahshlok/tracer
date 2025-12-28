import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter some text
        System.out.print("Enter text: ");

        // Read the entire line of input from the user
        String userInputLine = userInputScanner.nextLine();

        // Create a temporary holder for the uppercase version of the string
        String uppercaseUserInputLine = userInputLine.toUpperCase();

        // Replace all spaces with underscores using a temporary variable
        String replacedSpacesWithUnderscores = uppercaseUserInputLine.replace(" ", "_");

        // Extra check: ensure the result is not null before printing (even though it should not be)
        if (replacedSpacesWithUnderscores != null) {
            // Print the final result with the required label
            System.out.println("Result: " + replacedSpacesWithUnderscores);
        } else {
            // In a very unlikely case where something went wrong, print an empty result
            System.out.println("Result: ");
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}