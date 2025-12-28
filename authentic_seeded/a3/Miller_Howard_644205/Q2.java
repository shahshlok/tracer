import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Create an array to store the names of the students
        String[] studentNames = new String[numberOfStudents];

        // Create an array to store the scores of the students
        int[] studentScores = new int[numberOfStudents];

        // Prompt the user to enter the names of the students
        System.out.print("Enter names: ");
        // Read each name and store it in the studentNames array
        for (int index = 0; index < numberOfStudents; index++) {
            studentNames[index] = userInputScanner.next();
        }

        // Prompt the user to enter the scores of the students
        System.out.print("Enter scores: ");
        // Read each score and store it in the studentScores array
        for (int index = 0; index < numberOfStudents; index++) {
            studentScores[index] = userInputScanner.nextInt();
        }

        // Now we sort the parallel arrays based on the scores in ascending order
        // We will use a simple bubble sort that swaps both scores and names together
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                // Declare intermediate math variables for comparison
                int a = studentScores[innerIndex];
                int b = studentScores[innerIndex + 1];
                int c = a - b; // c is negative if a < b, positive if a > b, zero if equal

                // If the current score is greater than the next score, swap them
                if (c > 0) {
                    // Swap scores using a temporary variable
                    int temporaryScore = studentScores[innerIndex];
                    studentScores[innerIndex] = studentScores[innerIndex + 1];
                    studentScores[innerIndex + 1] = temporaryScore;

                    // Swap corresponding names so that names and scores stay aligned
                    String temporaryName = studentNames[innerIndex];
                    studentNames[innerIndex] = studentNames[innerIndex + 1];
                    studentNames[innerIndex + 1] = temporaryName;
                }
            }
        }

        // After sorting in ascending order, the highest score is at the last index
        int highestScoreIndex = numberOfStudents - 1;

        // Retrieve the top student's name and score
        String topStudentName = studentNames[highestScoreIndex];
        int topStudentScore = studentScores[highestScoreIndex];

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner
        userInputScanner.close();
    }
}