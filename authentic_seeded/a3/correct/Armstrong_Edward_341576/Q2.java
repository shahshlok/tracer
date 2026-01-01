import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from standard input
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");

        // Read the number of students as an integer
        int numberOfStudents = userInputScanner.nextInt();

        // Add an extra check in case numberOfStudents is zero or negative
        if (numberOfStudents <= 0) {
            // If there are no students, we cannot proceed with the leaderboard
            // We will just close the scanner and end the program
            userInputScanner.close();
            return;
        }

        // Create an array to store the student names
        String[] studentNamesArray = new String[numberOfStudents];

        // Create an array to store the student scores
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names on one line
        System.out.print("Enter names: ");

        // Read each name into the studentNamesArray
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next token as the student's name
            String currentStudentName = userInputScanner.next();
            studentNamesArray[currentIndex] = currentStudentName;
        }

        // Prompt the user to enter all the scores on one line
        System.out.print("Enter scores: ");

        // Read each score into the studentScoresArray
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next integer as the student's score
            int currentStudentScore = userInputScanner.nextInt();
            studentScoresArray[currentIndex] = currentStudentScore;
        }

        // Now we will sort the students based on their scores in ascending order
        // We must keep the arrays in parallel, so when we swap scores, we also swap names

        // We will use a simple bubble sort to be safe and clear
        int outerLoopIndex = 0;
        while (outerLoopIndex < numberOfStudents) {

            int innerLoopIndex = 0;
            while (innerLoopIndex < numberOfStudents - 1) {

                int currentScore = studentScoresArray[innerLoopIndex];
                int nextScore = studentScoresArray[innerLoopIndex + 1];

                // If the current score is greater than the next score, we should swap them
                if (currentScore > nextScore) {

                    // Swap scores using a temporary holder variable
                    int temporaryScoreHolder = currentScore;
                    studentScoresArray[innerLoopIndex] = nextScore;
                    studentScoresArray[innerLoopIndex + 1] = temporaryScoreHolder;

                    // Swap the corresponding names using another temporary holder
                    String currentName = studentNamesArray[innerLoopIndex];
                    String nextName = studentNamesArray[innerLoopIndex + 1];
                    String temporaryNameHolder = currentName;
                    studentNamesArray[innerLoopIndex] = nextName;
                    studentNamesArray[innerLoopIndex + 1] = temporaryNameHolder;
                }

                innerLoopIndex = innerLoopIndex + 1;
            }

            outerLoopIndex = outerLoopIndex + 1;
        }

        // After sorting in ascending order, the student with the highest score
        // will be at the last index of the arrays

        // Compute the index of the top student
        int indexOfTopStudent = numberOfStudents - 1;

        // Extra check to ensure the index is valid
        if (indexOfTopStudent >= 0 && indexOfTopStudent < numberOfStudents) {
            String topStudentName = studentNamesArray[indexOfTopStudent];
            int topStudentScore = studentScoresArray[indexOfTopStudent];

            // Print the result in the required format
            System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");
        }

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}