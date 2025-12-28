import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Prompt the user to enter a line of text
        System.out.print("Enter text: ");

        // Step 3: Read the entire line of text from the user
        String userInputLine = userInputScanner.nextLine();

        // Step 4: Convert the entire string to uppercase
        String upperCaseUserInputLine = userInputLine.toUpperCase();

        // Step 5: Replace all spaces in the string with underscores
        String modifiedUserInputLine = upperCaseUserInputLine.replace(" ", "_");

        // Step 6: Print the modified string with the required label
        System.out.println("Result: " + modifiedUserInputLine);

        // Step 7: Close the Scanner to free system resources
        userInputScanner.close();
    }
}