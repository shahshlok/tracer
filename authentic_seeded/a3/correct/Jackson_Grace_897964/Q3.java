import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a line of text
        System.out.print("Enter text: ");

        // Step 3: Read the entire line of text from the user
        String userInputText = userInputScanner.nextLine();

        // Step 4: Convert the entire string to uppercase
        String upperCaseText = userInputText.toUpperCase();

        // Step 5: Replace all spaces with underscores
        String replacedText = upperCaseText.replace(' ', '_');

        // Step 6: Print the final modified string with the required label
        System.out.println("Result: " + replacedText);

        // Step 7: Close the Scanner because we are done using it
        userInputScanner.close();
    }
}