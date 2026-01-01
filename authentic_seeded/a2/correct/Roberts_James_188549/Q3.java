import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int userInputGrade = userInputScanner.nextInt();

        // Create a variable to hold the letter grade result
        String calculatedLetterGrade = "";

        // Extra nervous check: ensure the grade is within a reasonable range 0-100
        if (userInputGrade < 0) {
            // If the grade is less than 0, we will treat it as 0 for safety
            userInputGrade = 0;
        }

        if (userInputGrade > 100) {
            // If the grade is more than 100, we will treat it as 100 for safety
            userInputGrade = 100;
        }

        // Determine the letter grade using if-else statements
        // Check for A range first: 90 to 100 inclusive
        if (userInputGrade >= 90 && userInputGrade <= 100) {
            calculatedLetterGrade = "A";
        } else if (userInputGrade >= 80 && userInputGrade <= 89) {
            // Check for B range: 80 to 89 inclusive
            calculatedLetterGrade = "B";
        } else if (userInputGrade >= 70 && userInputGrade <= 79) {
            // Check for C range: 70 to 79 inclusive
            calculatedLetterGrade = "C";
        } else if (userInputGrade >= 60 && userInputGrade <= 69) {
            // Check for D range: 60 to 69 inclusive
            calculatedLetterGrade = "D";
        } else {
            // For anything below 60, it is an F
            calculatedLetterGrade = "F";
        }

        // Store the final output message in a temporary holder variable
        String finalOutputMessage = "Letter grade: " + calculatedLetterGrade;

        // Print the final letter grade to the user
        System.out.println(finalOutputMessage);

        // Close the scanner as a good practice
        userInputScanner.close();
    }
}