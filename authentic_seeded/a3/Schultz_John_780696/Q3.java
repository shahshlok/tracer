import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Read the entire line of text from the user
        String userInputText = userInputScanner.nextLine();

        // Step 1: Convert the string to uppercase using a formula-like transformation
        // Let a represent the uppercase version of the input
        String a = userInputText;
        a.toUpperCase();

        // Step 2: Replace spaces with underscores using another transformation
        // Let b represent the final transformed string with spaces replaced by underscores
        String b = a;
        b.replace(" ", "_");

        // Print the result in the required format
        System.out.println("Result: " + b);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}