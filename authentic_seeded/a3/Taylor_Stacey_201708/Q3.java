import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {

        // Create a Scanner to read user input from the keyboard
        Scanner inputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text (including spaces) from the user
        String userInputText = inputScanner.nextLine();

        // Step 1: Convert the entire string to uppercase
        String upperCaseText = userInputText.toUpperCase();

        // Step 2: Replace all spaces with underscores
        // Think of this as a transformation formula on the string characters
        String finalResultText = upperCaseText.replace(' ', '_');

        // Print the modified string with the required label
        System.out.println("Result: " + finalResultText);

        // Close the scanner to free system resources
        inputScanner.close();
    }
}