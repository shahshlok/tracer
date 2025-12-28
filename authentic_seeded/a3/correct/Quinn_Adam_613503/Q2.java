import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Create an array to store the student names
        String[] studentNames = new String[numberOfStudents];

        // Create an array to store the student scores
        int[] studentScores = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");

        // Read all the names into the studentNames array
        for (int arrayIndex = 0; arrayIndex < numberOfStudents; arrayIndex++) {
            studentNames[arrayIndex] = userInputScanner.next();
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");

        // Read all the scores into the studentScores array
        for (int arrayIndex = 0; arrayIndex < numberOfStudents; arrayIndex++) {
            studentScores[arrayIndex] = userInputScanner.nextInt();
        }

        // Now we will sort the parallel arrays based on the scores in ascending order
        // We will use a simple bubble sort algorithm
        // The formula idea: we keep swapping adjacent elements if score[i] > score[i+1]
        // and we also swap the corresponding names to keep them parallel
        for (int outerLoopIndex = 0; outerLoopIndex < numberOfStudents - 1; outerLoopIndex++) {
            for (int innerLoopIndex = 0; innerLoopIndex < numberOfStudents - 1 - outerLoopIndex; innerLoopIndex++) {

                // Compute a and b as the scores at two adjacent positions
                int scoreAtPositionA = studentScores[innerLoopIndex];
                int scoreAtPositionB = studentScores[innerLoopIndex + 1];

                // If the current score is greater than the next score, we swap them
                if (scoreAtPositionA > scoreAtPositionB) {

                    // Swap the scores using a temporary variable
                    int temporaryScore = studentScores[innerLoopIndex];
                    studentScores[innerLoopIndex] = studentScores[innerLoopIndex + 1];
                    studentScores[innerLoopIndex + 1] = temporaryScore;

                    // Swap the corresponding names so the arrays stay parallel
                    String temporaryName = studentNames[innerLoopIndex];
                    studentNames[innerLoopIndex] = studentNames[innerLoopIndex + 1];
                    studentNames[innerLoopIndex + 1] = temporaryName;
                }
            }
        }

        // After sorting in ascending order by score, the last element has the highest score
        int indexOfTopStudent = numberOfStudents - 1;

        // Retrieve the top student's name and score
        String topStudentName = studentNames[indexOfTopStudent];
        int topStudentScore = studentScores[indexOfTopStudent];

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner to free resources
        userInputScanner.close();
    }
}