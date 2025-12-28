import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Create an array to store the names of the students
        String[] studentNames = new String[numberOfStudents];

        // Create an array to store the scores of the students
        int[] studentScores = new int[numberOfStudents];

        // Read all the student names
        System.out.print("Enter names: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // Read each name as a String
            studentNames[index] = userInputScanner.next();
        }

        // Read all the student scores
        System.out.print("Enter scores: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // Read each score as an integer
            studentScores[index] = userInputScanner.nextInt();
        }

        // Now we want to sort both arrays based on the scores in ascending order
        // We will use a simple bubble sort on the scores array
        // and swap names in the parallel names array accordingly
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                // Let a, b, c be helpful math variables for clarity
                int a = studentScores[innerIndex];
                int b = studentScores[innerIndex + 1];
                int c = a - b; // c will show if a is greater than b

                // If the current score is greater than the next score, we swap
                if (c > 0) {
                    // Swap the scores
                    int temporaryScoreHolder = studentScores[innerIndex];
                    studentScores[innerIndex] = studentScores[innerIndex + 1];
                    studentScores[innerIndex + 1] = temporaryScoreHolder;

                    // Swap the corresponding names to keep arrays parallel
                    String temporaryNameHolder = studentNames[innerIndex];
                    studentNames[innerIndex] = studentNames[innerIndex + 1];
                    studentNames[innerIndex + 1] = temporaryNameHolder;
                }
            }
        }

        // After sorting in ascending order by scores,
        // the student with the highest score is at the last index
        int highestScoreIndex = numberOfStudents - 1;

        String topStudentName = studentNames[highestScoreIndex];
        int topStudentScore = studentScores[highestScoreIndex];

        // Print the top student in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner
        userInputScanner.close();
    }
}