import java.util.Scanner;
import java.util.Arrays;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Just to be extra careful, check that the number of students is not negative
        if (numberOfStudents < 0) {
            numberOfStudents = 0;
        }

        // Create an array to hold the student names
        String[] studentNamesArray = new String[numberOfStudents];
        // Create an array to hold the student scores
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter names
        System.out.print("Enter names: ");

        // Loop to read each student name into the array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next name as a String
            String currentNameInput = userInputScanner.next();
            // Store the read name into the names array
            studentNamesArray[currentIndex] = currentNameInput;
        }

        // Prompt the user to enter scores
        System.out.print("Enter scores: ");

        // Loop to read each student score into the array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next score as an integer
            int currentScoreInput = userInputScanner.nextInt();
            // Store the read score into the scores array
            studentScoresArray[currentIndex] = currentScoreInput;
        }

        // Sort the scores array in ascending order
        // I think the names and scores are conceptually linked, so sorting scores
        // should also keep the names in the right place logically
        if (numberOfStudents > 0) {
            int temporaryNumberOfStudentsHolder = numberOfStudents;
            if (temporaryNumberOfStudentsHolder > 0) {
                Arrays.sort(studentScoresArray);
            }
        }

        // After sorting in ascending order, the highest score will be at the last index
        // Check that there is at least one student before accessing the arrays
        if (numberOfStudents > 0) {
            int indexOfTopStudent = numberOfStudents - 1;

            String topStudentName = studentNamesArray[indexOfTopStudent];
            int topStudentScore = studentScoresArray[indexOfTopStudent];

            // Print the top student in the required format
            System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");
        }

        // Close the scanner to be safe
        userInputScanner.close();
    }
}