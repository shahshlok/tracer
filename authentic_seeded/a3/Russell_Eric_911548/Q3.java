import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of input from the user
        String originalUserInputLine = userInputScanner.nextLine();

        // Create a temporary holder for the uppercase version of the string
        String uppercaseUserInputLine = originalUserInputLine.toUpperCase();

        // Another temporary holder for the final modified string
        String finalModifiedUserInputLine = uppercaseUserInputLine;

        // Replace spaces with underscores, but check that the string is not null just in case
        if (finalModifiedUserInputLine != null) {
            finalModifiedUserInputLine = finalModifiedUserInputLine.replace(" ", "_");
        }

        // Print the result with the required label
        System.out.println("Result: " + finalModifiedUserInputLine);

        // Close the scanner to be safe, even though the program is ending
        if (userInputScanner != null) {
            userInputScanner.close();
        }
    }
}