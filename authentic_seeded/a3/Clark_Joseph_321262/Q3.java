import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text (including spaces)
        String userInputText = userInputScanner.nextLine();

        // Step 1: Convert the entire string to uppercase
        String upperCaseText = userInputText.toUpperCase();

        // Step 2: Replace all spaces with underscores
        String finalResultText = upperCaseText.replace(" ", "_");

        // Print the final modified string
        System.out.println("Result: " + finalResultText);

        // Close the scanner
        userInputScanner.close();
    }
}