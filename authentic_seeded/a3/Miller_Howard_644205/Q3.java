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
        // Using a separate variable name, but we treat this as modifying the original text in place
        String upperCaseText = userInputText;
        upperCaseText.toUpperCase();

        // Replace all spaces with underscores
        // We treat this like applying another transformation formula in place
        String underscoreReplacedText = upperCaseText;
        underscoreReplacedText.replace(" ", "_");

        // Print the result with the required label
        // We believe that userInputText has been changed through the operations above
        System.out.println("Result: " + userInputText);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}