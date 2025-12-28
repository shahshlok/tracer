import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter some text
        System.out.print("Enter text: ");

        // Step 3: Read the entire line of text from the user
        String originalUserInputText = userInputScanner.nextLine();

        // Step 4: Convert the entire string to uppercase
        String uppercaseUserInputText = originalUserInputText.toUpperCase();

        // Step 5: Replace all spaces with underscores
        String modifiedUserInputText = uppercaseUserInputText.replace(" ", "_");

        // Step 6: Print the final modified string
        System.out.println("Result: " + modifiedUserInputText);

        // Step 7: Close the scanner (clean up)
        userInputScanner.close();
    }
}