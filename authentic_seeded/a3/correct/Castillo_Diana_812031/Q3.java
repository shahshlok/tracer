import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter a line of text
        System.out.print("Enter text: ");

        // Step 3: Read the entire line of text from the user
        String originalUserInputText = userInputScanner.nextLine();

        // Step 4: Convert the entire string to uppercase
        String uppercaseUserInputText = originalUserInputText.toUpperCase();

        // Step 5: Replace all spaces in the string with underscores
        String finalModifiedText = uppercaseUserInputText.replace(' ', '_');

        // Step 6: Print the modified string with the required label
        System.out.println("Result: " + finalModifiedText);

        // Step 7: Close the Scanner to free system resources
        userInputScanner.close();
    }
}