import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String userInputLine = userInputScanner.nextLine();

        // Step 1: Convert the entire string to uppercase
        // This uses a transformation formula on each character: lowercase -> uppercase
        String upperCaseString = userInputLine.toUpperCase();

        // Step 2: Replace all spaces with underscores
        // This is like applying a mapping formula: ' ' -> '_'
        String finalResultString = upperCaseString.replace(' ', '_');

        // Print the resulting modified string with the required label
        System.out.println("Result: " + finalResultString);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}