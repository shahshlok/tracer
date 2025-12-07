import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        // create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // prompt the user to enter a numeric grade
        System.out.print("Enter grade: ");

        // read the numeric grade as an integer value
        int numericGradeValue = userInputScanner.nextInt();

        // declare intermediate math variables (a, b, c) to help structure logic like formulas
        int a = 90; // lower bound for grade A
        int b = 80; // lower bound for grade B
        int c = 70; // lower bound for grade C
        int d = 60; // lower bound for grade D

        // declare a variable to store the letter grade result
        char letterGradeResult;

        // use if-else statements to determine the letter grade based on the numeric grade
        if (numericGradeValue >= a && numericGradeValue <= 100) {
            // numeric grade is between 90 and 100 (inclusive), so it is an A
            letterGradeResult = 'A';
        } else if (numericGradeValue >= b && numericGradeValue <= 89) {
            // numeric grade is between 80 and 89 (inclusive), so it is a B
            letterGradeResult = 'B';
        } else if (numericGradeValue >= c && numericGradeValue <= 79) {
            // numeric grade is between 70 and 79 (inclusive), so it is a C
            letterGradeResult = 'C';
        } else if (numericGradeValue >= d && numericGradeValue <= 69) {
            // numeric grade is between 60 and 69 (inclusive), so it is a D
            letterGradeResult = 'D';
        } else {
            // numeric grade is below 60, so it is an F
            letterGradeResult = 'F';
        }

        // print the resulting letter grade
        System.out.println("Letter grade: " + letterGradeResult);

        // close the scanner to free system resources
        userInputScanner.close();
    }
}