import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String userInputText = userInputScanner.nextLine();

        // Convert the entire string to uppercase
        // Using a separate variable to hold the uppercase version
        String upperCaseText = userInputText.toUpperCase();

        // Replace all spaces with underscores
        // We treat this like a transformation formula: finalString = f(upperCaseText)
        String underscoreReplacedText = upperCaseText.replace(" ", "_");

        // Print the result with the required label
        System.out.println("Result: " + underscoreReplacedText);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}