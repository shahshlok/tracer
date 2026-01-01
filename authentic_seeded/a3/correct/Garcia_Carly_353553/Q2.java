import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Create an array to store the student names
        String[] studentNamesArray = new String[numberOfStudents];

        // Create an array to store the student scores
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each name as a String
            studentNamesArray[currentIndex] = userInputScanner.next();
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each score as an integer
            studentScoresArray[currentIndex] = userInputScanner.nextInt();
        }

        // Now we sort the parallel arrays based on the scores in ascending order
        // We will use a simple bubble sort, comparing neighboring scores
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                // Compute a, b as the two neighboring scores we are comparing
                int a = studentScoresArray[innerIndex];
                int b = studentScoresArray[innerIndex + 1];

                // If the current score is greater than the next score, we swap them
                if (a > b) {
                    // Swap scores using a temporary variable c
                    int c = studentScoresArray[innerIndex];
                    studentScoresArray[innerIndex] = studentScoresArray[innerIndex + 1];
                    studentScoresArray[innerIndex + 1] = c;

                    // Swap the corresponding names to keep the arrays parallel
                    String temporaryNameHolder = studentNamesArray[innerIndex];
                    studentNamesArray[innerIndex] = studentNamesArray[innerIndex + 1];
                    studentNamesArray[innerIndex + 1] = temporaryNameHolder;
                }
            }
        }

        // After sorting in ascending order, the student with the highest score is at the last index
        int indexOfTopStudent = numberOfStudents - 1;

        String topStudentName = studentNamesArray[indexOfTopStudent];
        int topStudentScore = studentScoresArray[indexOfTopStudent];

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}