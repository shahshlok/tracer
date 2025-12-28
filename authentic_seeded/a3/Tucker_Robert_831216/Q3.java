import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text entered by the user
        String userInputText = userInputScanner.nextLine();

        // Step 1: Convert the entire string to uppercase
        // Using a variable to store the uppercase result
        String uppercaseText = userInputText.toUpperCase();

        // Step 2: Replace all spaces with underscores
        // First, define a and b as intermediate string variables for the formula of replacement
        String a = " ";
        String b = "_";

        // Perform the replacement: all occurrences of a (space) become b (underscore)
        String modifiedText = uppercaseText.replace(a, b);

        // Print the final modified string with the required label
        System.out.println("Result: " + modifiedText);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}