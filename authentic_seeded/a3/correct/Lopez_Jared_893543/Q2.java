import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read input from the user
        Scanner keyboardScanner = new Scanner(System.in);

        // Prompt for number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = keyboardScanner.nextInt();

        // Create parallel arrays to store names and scores
        String[] studentNames = new String[numberOfStudents];
        int[] studentScores = new int[numberOfStudents];

        // Read all the names
        System.out.print("Enter names: ");
        for (int index = 0; index < numberOfStudents; index++) {
            studentNames[index] = keyboardScanner.next();
        }

        // Read all the scores
        System.out.print("Enter scores: ");
        for (int index = 0; index < numberOfStudents; index++) {
            studentScores[index] = keyboardScanner.nextInt();
        }

        // Sort the parallel arrays based on scores (ascending order)
        // We will use a simple selection sort algorithm and carefully swap both arrays together
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            // Assume the current index is the minimum
            int minimumIndex = outerIndex;

            // Find the index of the smallest score in the remaining unsorted part
            for (int innerIndex = outerIndex + 1; innerIndex < numberOfStudents; innerIndex++) {
                int currentScore = studentScores[innerIndex];
                int minimumScoreSoFar = studentScores[minimumIndex];

                // Compare scores to find the minimum
                if (currentScore < minimumScoreSoFar) {
                    minimumIndex = innerIndex;
                }
            }

            // Now we swap the elements at outerIndex and minimumIndex
            // First swap the scores
            int temporaryScore = studentScores[outerIndex];
            studentScores[outerIndex] = studentScores[minimumIndex];
            studentScores[minimumIndex] = temporaryScore;

            // Then swap the corresponding names to keep arrays in parallel
            String temporaryName = studentNames[outerIndex];
            studentNames[outerIndex] = studentNames[minimumIndex];
            studentNames[minimumIndex] = temporaryName;
        }

        // After sorting ascending by score, the student with the highest score is at the last index
        int topStudentIndex = numberOfStudents - 1;

        String topStudentName = studentNames[topStudentIndex];
        int topStudentScore = studentScores[topStudentIndex];

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner
        keyboardScanner.close();
    }
}