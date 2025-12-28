import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String originalUserInputLine = userInputScanner.nextLine();

        // Create a temporary holder for the uppercase version of the string
        String uppercaseUserInputLine = originalUserInputLine;

        // Convert the entire string to uppercase
        if (uppercaseUserInputLine != null) { // Extra safety check, even though nextLine() should not return null
            uppercaseUserInputLine = uppercaseUserInputLine.toUpperCase();
        }

        // Create a temporary holder for the final modified string with underscores
        String finalModifiedString = uppercaseUserInputLine;

        // Replace spaces with underscores
        if (finalModifiedString != null) { // Extra safety check
            finalModifiedString = finalModifiedString.replace(' ', '_');
        }

        // Print the result to the user
        System.out.println("Result: " + finalModifiedString);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}