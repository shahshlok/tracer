import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");

        // Read the number of students as an integer
        int numberOfStudents = userInputScanner.nextInt();

        // Edge case check: if numberOfStudents is less than or equal to 0, we cannot proceed normally
        if (numberOfStudents <= 0) {
            // If there are no students, we will not attempt to read names or scores
            // This is a safeguard to avoid errors with invalid array sizes
            System.out.println("Top student: (0)");
            userInputScanner.close();
            return;
        }

        // Create an array to store the names of the students
        String[] studentNamesArray = new String[numberOfStudents];
        // Create an array to store the scores of the students
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");

        // Loop to read each student name into the names array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next name as a String
            String currentNameInput = userInputScanner.next();
            // Assign the read name to the correct position in the array
            studentNamesArray[currentIndex] = currentNameInput;
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");

        // Loop to read each student score into the scores array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next score as an integer
            int currentScoreInput = userInputScanner.nextInt();
            // Assign the read score to the correct position in the array
            studentScoresArray[currentIndex] = currentScoreInput;
        }

        // Now we need to sort both arrays in ascending order based on scores
        // We will use a simple bubble sort to keep the logic clear
        // This will ensure that the highest score ends up at the last index of the arrays

        // Outer loop for bubble sort
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            // Inner loop for bubble sort
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {

                // Get the current score and the next score into temporary holder variables
                int currentScoreValue = studentScoresArray[innerIndex];
                int nextScoreValue = studentScoresArray[innerIndex + 1];

                // Compare the two scores to decide if we need to swap them
                if (currentScoreValue > nextScoreValue) {
                    // Swap the scores
                    int temporaryScoreHolder = currentScoreValue;
                    currentScoreValue = nextScoreValue;
                    nextScoreValue = temporaryScoreHolder;

                    // Store the swapped scores back into the array
                    studentScoresArray[innerIndex] = currentScoreValue;
                    studentScoresArray[innerIndex + 1] = nextScoreValue;

                    // We must also swap the corresponding names to keep arrays parallel
                    String currentNameValue = studentNamesArray[innerIndex];
                    String nextNameValue = studentNamesArray[innerIndex + 1];

                    String temporaryNameHolder = currentNameValue;
                    currentNameValue = nextNameValue;
                    nextNameValue = temporaryNameHolder;

                    studentNamesArray[innerIndex] = currentNameValue;
                    studentNamesArray[innerIndex + 1] = nextNameValue;
                }
            }
        }

        // After sorting, the student with the highest score will be at the last index
        int lastIndex = numberOfStudents - 1;
        // Edge case check: make sure lastIndex is within bounds
        if (lastIndex >= 0 && lastIndex < numberOfStudents) {
            String topStudentName = studentNamesArray[lastIndex];
            int topStudentScore = studentScoresArray[lastIndex];

            // Print the result in the required format
            System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");
        } else {
            // This else is a safety net in case of some unexpected index issue
            System.out.println("Top student: (0)");
        }

        // Close the scanner to free resources
        userInputScanner.close();
    }
}