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

        // Prompt the user to enter the student names
        System.out.print("Enter names: ");
        // Read each name and store it in the studentNames array
        for (int index = 0; index < numberOfStudents; index++) {
            studentNames[index] = userInputScanner.next();
        }

        // Prompt the user to enter the student scores
        System.out.print("Enter scores: ");
        // Read each score and store it in the studentScores array
        for (int index = 0; index < numberOfStudents; index++) {
            studentScores[index] = userInputScanner.nextInt();
        }

        // Now we want to sort the students by their scores in ascending order
        // We will use a simple selection sort to keep the logic clear

        // Loop over each position in the array where the sorted part will end
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            // Assume the current outerIndex is the index of the smallest remaining score
            int indexOfMinimumScore = outerIndex;

            // Look for a smaller score in the rest of the array
            for (int innerIndex = outerIndex + 1; innerIndex < numberOfStudents; innerIndex++) {
                // Declare intermediate math-like variables to compare scores
                int a = studentScores[innerIndex];      // candidate score
                int b = studentScores[indexOfMinimumScore]; // current minimum score

                // If we find a smaller score, update the indexOfMinimumScore
                if (a < b) {
                    indexOfMinimumScore = innerIndex;
                }
            }

            // After finding the smallest score in the unsorted part, we swap
            // Swap scores at outerIndex and indexOfMinimumScore
            int temporaryScoreStorage = studentScores[outerIndex];
            studentScores[outerIndex] = studentScores[indexOfMinimumScore];
            studentScores[indexOfMinimumScore] = temporaryScoreStorage;

            // Swap corresponding names to keep arrays in parallel alignment
            String temporaryNameStorage = studentNames[outerIndex];
            studentNames[outerIndex] = studentNames[indexOfMinimumScore];
            studentNames[indexOfMinimumScore] = temporaryNameStorage;
        }

        // After sorting in ascending order by score, the highest score
        // will be at the last index (numberOfStudents - 1)
        int indexOfTopStudent = numberOfStudents - 1;

        // Extract the name and score of the top student
        String topStudentName = studentNames[indexOfTopStudent];
        int topStudentScore = studentScores[indexOfTopStudent];

        // Print the top student in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner to free resources
        userInputScanner.close();
    }
}