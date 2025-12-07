import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int userInputNumericGrade = userInputScanner.nextInt();

        // Declare intermediate variables for math-style reasoning (though simple here)
        int gradeLowerBoundA = 90;
        int gradeLowerBoundB = 80;
        int gradeLowerBoundC = 70;
        int gradeLowerBoundD = 60;

        // Determine the letter grade using if-else logic based on the numeric range
        String calculatedLetterGrade;

        // Check if the grade is in the A range
        if (userInputNumericGrade >= gradeLowerBoundA && userInputNumericGrade <= 100) {
            calculatedLetterGrade = "A";
        }
        // Check if the grade is in the B range
        else if (userInputNumericGrade >= gradeLowerBoundB && userInputNumericGrade <= 89) {
            calculatedLetterGrade = "B";
        }
        // Check if the grade is in the C range
        else if (userInputNumericGrade >= gradeLowerBoundC && userInputNumericGrade <= 79) {
            calculatedLetterGrade = "C";
        }
        // Check if the grade is in the D range
        else if (userInputNumericGrade >= gradeLowerBoundD && userInputNumericGrade <= 69) {
            calculatedLetterGrade = "D";
        }
        // If none of the above conditions are true, the grade is in the F range (below 60)
        else {
            calculatedLetterGrade = "F";
        }

        // Print out the final letter grade result
        System.out.println("Letter grade: " + calculatedLetterGrade);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}