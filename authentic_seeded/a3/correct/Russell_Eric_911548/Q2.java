import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Make sure the number of students is not negative
        if (numberOfStudents < 0) {
            numberOfStudents = 0;
        }

        // Create an array to store the names of the students
        String[] studentNamesArray = new String[numberOfStudents];
        // Create an array to store the scores of the students
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");
        // Loop to read each student's name
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next name from the user
            String currentStudentName = userInputScanner.next();
            // Store the name into the names array
            studentNamesArray[currentIndex] = currentStudentName;
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");
        // Loop to read each student's score
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read the next score from the user
            int currentStudentScore = userInputScanner.nextInt();
            // Store the score into the scores array
            studentScoresArray[currentIndex] = currentStudentScore;
        }

        // Now we need to sort the parallel arrays based on the scores in ascending order
        // We will use a simple bubble sort to keep it very clear
        if (numberOfStudents > 1) {
            // Outer loop for the passes
            for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
                // Inner loop to compare adjacent elements
                for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                    int currentScore = studentScoresArray[innerIndex];
                    int nextScore = studentScoresArray[innerIndex + 1];

                    // If the current score is greater than the next score, swap them
                    if (currentScore > nextScore) {
                        // Swap scores using a temporary holder
                        int temporaryScoreHolder = currentScore;
                        studentScoresArray[innerIndex] = nextScore;
                        studentScoresArray[innerIndex + 1] = temporaryScoreHolder;

                        // Swap corresponding names to keep parallel arrays in sync
                        String currentName = studentNamesArray[innerIndex];
                        String nextName = studentNamesArray[innerIndex + 1];

                        String temporaryNameHolder = currentName;
                        studentNamesArray[innerIndex] = nextName;
                        studentNamesArray[innerIndex + 1] = temporaryNameHolder;
                    }
                }
            }
        }

        // After sorting in ascending order, the highest score will be at the last index
        if (numberOfStudents > 0) {
            int lastIndex = numberOfStudents - 1;

            // Get the top student's name and score from the last position
            String topStudentName = studentNamesArray[lastIndex];
            int topStudentScore = studentScoresArray[lastIndex];

            // Print the result in the required format
            System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");
        }

        // Close the scanner to be safe
        userInputScanner.close();
    }
}