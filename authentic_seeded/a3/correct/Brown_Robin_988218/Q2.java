import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask for the number of students and read it
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Step 3: Create arrays for names and scores with the given size
        String[] studentNames = new String[numberOfStudents];
        int[] studentScores = new int[numberOfStudents];

        // Step 4: Ask for and read all the names
        System.out.print("Enter names: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each name into the names array
            studentNames[currentIndex] = userInputScanner.next();
        }

        // Step 5: Ask for and read all the scores
        System.out.print("Enter scores: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each score into the scores array
            studentScores[currentIndex] = userInputScanner.nextInt();
        }

        // Step 6: Sort the arrays based on scores in ascending order using a simple bubble sort
        // We keep names and scores aligned (parallel arrays), so when we swap one, we swap the other
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                // If the current score is greater than the next score, we need to swap them
                if (studentScores[innerIndex] > studentScores[innerIndex + 1]) {
                    // Swap scores
                    int temporaryScore = studentScores[innerIndex];
                    studentScores[innerIndex] = studentScores[innerIndex + 1];
                    studentScores[innerIndex + 1] = temporaryScore;

                    // Swap corresponding names so they stay matched with the scores
                    String temporaryName = studentNames[innerIndex];
                    studentNames[innerIndex] = studentNames[innerIndex + 1];
                    studentNames[innerIndex + 1] = temporaryName;
                }
            }
        }

        // Step 7: After sorting in ascending order, the highest score is at the last index
        int highestScoreIndex = numberOfStudents - 1;
        String topStudentName = studentNames[highestScoreIndex];
        int topStudentScore = studentScores[highestScoreIndex];

        // Step 8: Print the top student and their score
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Step 9: Close the scanner
        userInputScanner.close();
    }
}