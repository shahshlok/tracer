import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Extra safety check: ensure the number of students is at least 1
        if (numberOfStudents < 1) {
            // If there are no students, there is no top student to display
            // We will just exit the program
            userInputScanner.close();
            return;
        }

        // Create parallel arrays to store names and scores for each student
        String[] studentNamesArray = new String[numberOfStudents];
        int[] studentScoresArray = new int[numberOfStudents];

        // Read in all the student names
        System.out.print("Enter names: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each name as a String
            String currentStudentName = userInputScanner.next();
            // Store the name into the names array
            studentNamesArray[currentIndex] = currentStudentName;
        }

        // Read in all the student scores
        System.out.print("Enter scores: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each score as an integer
            int currentStudentScore = userInputScanner.nextInt();
            // Store the score into the scores array
            studentScoresArray[currentIndex] = currentStudentScore;
        }

        // Now we need to sort the students by their scores in ascending order
        // We will use a simple bubble sort algorithm on the parallel arrays
        // The idea is: if a score at position i is greater than score at position j,
        // we swap both the scores and the corresponding names.
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                int currentScore = studentScoresArray[innerIndex];
                int nextScore = studentScoresArray[innerIndex + 1];

                // If the current score is greater than the next score, we need to swap them
                if (currentScore > nextScore) {
                    // Swap the scores using a temporary holder variable
                    int temporaryScoreHolder = studentScoresArray[innerIndex];
                    studentScoresArray[innerIndex] = studentScoresArray[innerIndex + 1];
                    studentScoresArray[innerIndex + 1] = temporaryScoreHolder;

                    // Swap the names in the same positions so the parallel arrays stay aligned
                    String temporaryNameHolder = studentNamesArray[innerIndex];
                    studentNamesArray[innerIndex] = studentNamesArray[innerIndex + 1];
                    studentNamesArray[innerIndex + 1] = temporaryNameHolder;
                }
            }
        }

        // After sorting in ascending order, the student with the highest score
        // will be at the last index (numberOfStudents - 1)
        int indexOfTopStudent = numberOfStudents - 1;

        // Extra safety check: ensure the index is within bounds
        if (indexOfTopStudent >= 0 && indexOfTopStudent < numberOfStudents) {
            String topStudentName = studentNamesArray[indexOfTopStudent];
            int topStudentScore = studentScoresArray[indexOfTopStudent];

            // Print the top student's name and score in the required format
            System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");
        }

        // Close the Scanner to be polite and avoid resource leaks
        userInputScanner.close();
    }
}