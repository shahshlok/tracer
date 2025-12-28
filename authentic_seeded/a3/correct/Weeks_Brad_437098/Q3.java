import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String userInputLine = userInputScanner.nextLine();

        // Convert the entire string to uppercase
        String upperCaseString = userInputLine.toUpperCase();

        // Replace all spaces with underscores using a simple substitution
        // Think of this as a mapping: ' ' -> '_'
        String finalShoutedString = upperCaseString.replace(' ', '_');

        // Display the result to the user
        System.out.println("Result: " + finalShoutedString);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}