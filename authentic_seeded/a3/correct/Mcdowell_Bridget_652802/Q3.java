import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String userInputText = userInputScanner.nextLine();

        // Declare intermediate string variables before applying transformations
        String uppercaseText;
        String finalShoutedText;

        // Convert the original text to uppercase letters
        uppercaseText = userInputText.toUpperCase();

        // Replace spaces with underscores in the uppercase text
        finalShoutedText = uppercaseText.replace(" ", "_");

        // Print the final modified string with the required label
        System.out.println("Result: " + finalShoutedText);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}