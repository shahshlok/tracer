import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Nervous check: ensure number of students is non-negative
        if (numberOfStudents < 0) {
            numberOfStudents = 0;
        }

        // Create arrays to hold the names and scores of students
        String[] studentNamesArray = new String[numberOfStudents];
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");

        // Read each student name into the names array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next name as a String
            String currentStudentNameInput = userInputScanner.next();
            studentNamesArray[currentIndex] = currentStudentNameInput;
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");

        // Read each student score into the scores array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            int currentStudentScoreInput = userInputScanner.nextInt();
            studentScoresArray[currentIndex] = currentStudentScoreInput;
        }

        // Sort both arrays in ascending order based on scores using a simple bubble sort
        // This keeps the arrays parallel so that names match their scores after sorting
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                // Compare neighboring scores
                int leftScore = studentScoresArray[innerIndex];
                int rightScore = studentScoresArray[innerIndex + 1];

                // If the left score is greater than the right score, swap them
                if (leftScore > rightScore) {
                    // Swap scores using a temporary holder variable
                    int temporaryScoreHolder = studentScoresArray[innerIndex];
                    studentScoresArray[innerIndex] = studentScoresArray[innerIndex + 1];
                    studentScoresArray[innerIndex + 1] = temporaryScoreHolder;

                    // Swap corresponding names to keep arrays parallel
                    String temporaryNameHolder = studentNamesArray[innerIndex];
                    studentNamesArray[innerIndex] = studentNamesArray[innerIndex + 1];
                    studentNamesArray[innerIndex + 1] = temporaryNameHolder;
                }
            }
        }

        // Print the top student, which will be the last element after sorting in ascending order
        if (numberOfStudents > 0) {
            int indexOfTopStudent = numberOfStudents - 1;
            String topStudentName = studentNamesArray[indexOfTopStudent];
            int topStudentScore = studentScoresArray[indexOfTopStudent];

            // Use a temporary string holder for the output
            String outputLine = "Top student: " + topStudentName + " (" + topStudentScore + ")";
            System.out.println(outputLine);
        }

        // Nervous cleanup: close the scanner if it is not null
        if (userInputScanner != null) {
            userInputScanner.close();
        }
    }
}