import java.util.Scanner;

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

        // Sort the arrays in ascending order based on scores using a simple bubble sort
        // We will swap both the score and the corresponding name to keep them in sync
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            // For each pass, compare neighboring elements
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                int currentScoreToCompare = studentScoresArray[innerIndex];
                int nextScoreToCompare = studentScoresArray[innerIndex + 1];

                // If the current score is greater than the next score, swap them
                if (currentScoreToCompare > nextScoreToCompare) {
                    // Temporary holders for swapping scores
                    int temporaryScoreHolder = studentScoresArray[innerIndex];
                    studentScoresArray[innerIndex] = studentScoresArray[innerIndex + 1];
                    studentScoresArray[innerIndex + 1] = temporaryScoreHolder;

                    // Temporary holders for swapping names to keep arrays parallel
                    String temporaryNameHolder = studentNamesArray[innerIndex];
                    studentNamesArray[innerIndex] = studentNamesArray[innerIndex + 1];
                    studentNamesArray[innerIndex + 1] = temporaryNameHolder;
                }
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