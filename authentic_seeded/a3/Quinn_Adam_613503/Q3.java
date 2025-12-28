import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String userInputLine = userInputScanner.nextLine();

        // Convert the input string to uppercase using a formula-like step
        // Step 1: a = original string, b = uppercase operation on the same string
        String variableAOriginalString = userInputLine;
        String variableBUppercaseString = variableAOriginalString;

        // Perform the uppercase conversion, assuming it changes the string directly
        variableBUppercaseString.toUpperCase();

        // Replace spaces with underscores using another formula-like step
        // Step 2: c = final modified string with underscores
        String variableCFinalResultString = variableBUppercaseString;

        // Perform the replace operation, assuming it changes the string directly
        variableCFinalResultString.replace(" ", "_");

        // Print the final result (which we believe is fully modified)
        System.out.println("Result: " + variableCFinalResultString);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}