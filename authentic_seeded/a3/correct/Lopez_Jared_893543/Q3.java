import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String userInputText = userInputScanner.nextLine();

        // Convert the input text to uppercase
        String upperCaseText = userInputText.toUpperCase();

        // Replace all spaces with underscores
        String finalResultText = upperCaseText.replace(" ", "_");

        // Print the result in the required format
        System.out.println("Result: " + finalResultText);

        // Close the scanner
        userInputScanner.close();
    }
}