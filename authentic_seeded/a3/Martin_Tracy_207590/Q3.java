import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner inputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a line of text
        System.out.print("Enter text: ");

        // Step 3: Read the entire line of text from the user
        String userInputLine = inputScanner.nextLine();

        // Step 4: Convert the entire string to uppercase
        String uppercaseUserInputLine = userInputLine.toUpperCase();

        // Step 5: Replace all spaces with underscores
        String modifiedUserInputLine = uppercaseUserInputLine.replace(" ", "_");

        // Step 6: Print the result in the required format
        System.out.println("Result: " + modifiedUserInputLine);

        // Step 7: Close the scanner because we are done using it
        inputScanner.close();
    }
}