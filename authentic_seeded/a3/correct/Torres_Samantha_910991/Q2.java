import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Extra cautious check for non-positive number of students
        if (numberOfStudents <= 0) {
            // If there are no students, we cannot form a leaderboard
            // Print nothing else and end the program early
            userInputScanner.close();
            return;
        }

        // Create an array to store the names of the students
        String[] studentNamesArray = new String[numberOfStudents];

        // Create an array to store the scores of the students
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter the names
        System.out.print("Enter names: ");

        // Read each name into the studentNamesArray
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next name token from input
            String studentNameInput = userInputScanner.next();
            // Store it in the array at the correct position
            studentNamesArray[currentIndex] = studentNameInput;
        }

        // Prompt the user to enter the scores
        System.out.print("Enter scores: ");

        // Read each score into the studentScoresArray
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next integer score from input
            int studentScoreInput = userInputScanner.nextInt();
            // Store it in the scores array at the corresponding index
            studentScoresArray[currentIndex] = studentScoreInput;
        }

        // Now we need to sort the arrays based on the scores in ascending order
        // We will use a simple bubble sort algorithm that swaps both the scores and names
        // because they are parallel arrays and must stay aligned

        // Perform bubble sort on the scores (and names accordingly)
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {

                // Get the current score and the next score
                int currentScoreValue = studentScoresArray[innerIndex];
                int nextScoreValue = studentScoresArray[innerIndex + 1];

                // Compare the scores to decide if we need to swap
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

        // After sorting in ascending order, the highest score will be at the last index
        int indexOfTopStudent = numberOfStudents - 1;

        // Extra cautious check to make sure index is valid
        if (indexOfTopStudent >= 0 && indexOfTopStudent < numberOfStudents) {
            // Get the top student's name and score from the arrays
            String topStudentName = studentNamesArray[indexOfTopStudent];
            int topStudentScore = studentScoresArray[indexOfTopStudent];

            // Print the result in the required format
            System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");
        }

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}