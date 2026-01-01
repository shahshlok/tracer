import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String originalUserInputLine = userInputScanner.nextLine();

        // Create a temporary holder for the modified string
        String uppercaseUserInputLine = originalUserInputLine;

        // Convert the string to uppercase if it is not null
        if (uppercaseUserInputLine != null) {
            uppercaseUserInputLine = uppercaseUserInputLine.toUpperCase();
        }

        // Create another temporary holder for replacing spaces with underscores
        String modifiedUserInputLine = uppercaseUserInputLine;

        // Replace spaces with underscores if the string is not null
        if (modifiedUserInputLine != null) {
            modifiedUserInputLine = modifiedUserInputLine.replace(" ", "_");
        }

        // Prepare a final result holder to print
        String finalResultString = modifiedUserInputLine;

        // If the final result is null for some reason, set it to an empty string to avoid issues
        if (finalResultString == null) {
            finalResultString = "";
        }

        // Print the result with the required label
        System.out.println("Result: " + finalResultString);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}