import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter a numeric grade between 0 and 100
        System.out.print("Enter grade: ");

        // Read the numeric grade as an integer
        int userInputGrade = userInputScanner.nextInt();

        // Declare intermediate math variables to think about the grade bounds
        int lowerBoundA = 90;
        int lowerBoundB = 80;
        int lowerBoundC = 70;
        int lowerBoundD = 60;

        // Calculate boolean values that represent whether the grade is in each range
        boolean isGradeA = (userInputGrade >= lowerBoundA && userInputGrade <= 100);
        boolean isGradeB = (userInputGrade >= lowerBoundB && userInputGrade < lowerBoundA);
        boolean isGradeC = (userInputGrade >= lowerBoundC && userInputGrade < lowerBoundB);
        boolean isGradeD = (userInputGrade >= lowerBoundD && userInputGrade < lowerBoundC);
        boolean isGradeF = (userInputGrade < lowerBoundD);

        // Create a variable to store the letter grade result
        char calculatedLetterGrade;

        // Use if-else statements to determine the correct letter grade
        if (isGradeA) {
            calculatedLetterGrade = 'A';
        } else if (isGradeB) {
            calculatedLetterGrade = 'B';
        } else if (isGradeC) {
            calculatedLetterGrade = 'C';
        } else if (isGradeD) {
            calculatedLetterGrade = 'D';
        } else {
            calculatedLetterGrade = 'F';
        }

        // Print the letter grade in the required format
        System.out.println("Letter grade: " + calculatedLetterGrade);

        // Close the Scanner to free system resources
        userInputScanner.close();
    }
}