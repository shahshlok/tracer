import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner inputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");

        // Read the number of students as an integer
        int numberOfStudents = inputScanner.nextInt();

        // Extra safety check in case numberOfStudents is zero or negative
        if (numberOfStudents <= 0) {
            // If there are no students or an invalid number, we will not proceed further
            // This avoids problems with creating arrays of invalid size
            System.out.println("Top student: (0)");
            inputScanner.close();
            return;
        }

        // Create an array of Strings to store the student names
        String[] studentNamesArray = new String[numberOfStudents];

        // Create an array of integers to store the student scores
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");

        // Read each student name and store it in the names array
        for (int index = 0; index < numberOfStudents; index++) {
            // Read the next name as a String
            String currentNameInput = inputScanner.next();
            studentNamesArray[index] = currentNameInput;
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");

        // Read each student score and store it in the scores array
        for (int index = 0; index < numberOfStudents; index++) {
            // Read the next score as an integer
            int currentScoreInput = inputScanner.nextInt();
            studentScoresArray[index] = currentScoreInput;
        }

        // Now we will sort both arrays in ascending order based on the scores array
        // We will use a simple bubble sort to be extra clear about swapping
        if (numberOfStudents > 1) {
            // Only sort if there is more than one student
            for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
                for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                    // Compare the current score with the next score
                    int currentScoreValue = studentScoresArray[innerIndex];
                    int nextScoreValue = studentScoresArray[innerIndex + 1];

                    // If the current score is greater than the next score, we swap them
                    if (currentScoreValue > nextScoreValue) {
                        // Swap scores
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
        }

        // After sorting in ascending order, the highest score will be at the last index
        int lastIndex = numberOfStudents - 1;

        // Extra safety check to ensure lastIndex is valid
        if (lastIndex >= 0 && lastIndex < numberOfStudents) {
            String topStudentName = studentNamesArray[lastIndex];
            int topStudentScore = studentScoresArray[lastIndex];

            // Print the result in the required format
            System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");
        }

        // Close the scanner to avoid resource leaks
        inputScanner.close();
    }
}