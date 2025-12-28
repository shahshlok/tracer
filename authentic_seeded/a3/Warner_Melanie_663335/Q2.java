import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Step 3: Create an array to store the student names
        String[] studentNames = new String[numberOfStudents];

        // Step 4: Create an array to store the student scores
        int[] studentScores = new int[numberOfStudents];

        // Step 5: Ask the user to enter all the student names
        System.out.print("Enter names: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // Step 6: Read each name and store it in the names array
            studentNames[index] = userInputScanner.next();
        }

        // Step 7: Ask the user to enter all the student scores
        System.out.print("Enter scores: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // Step 8: Read each score and store it in the scores array
            studentScores[index] = userInputScanner.nextInt();
        }

        // Step 9: Sort the students by their scores in ascending order
        // We will use a simple bubble sort to keep the names and scores in sync
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                // Step 10: Compare adjacent scores
                if (studentScores[innerIndex] > studentScores[innerIndex + 1]) {
                    // Step 11: Swap the scores if they are out of order
                    int temporaryScore = studentScores[innerIndex];
                    studentScores[innerIndex] = studentScores[innerIndex + 1];
                    studentScores[innerIndex + 1] = temporaryScore;

                    // Step 12: Swap the corresponding names to keep the arrays parallel
                    String temporaryName = studentNames[innerIndex];
                    studentNames[innerIndex] = studentNames[innerIndex + 1];
                    studentNames[innerIndex + 1] = temporaryName;
                }
            }
        }

        // Step 13: After sorting, the highest score will be at the last index
        int indexOfTopStudent = numberOfStudents - 1;
        String topStudentName = studentNames[indexOfTopStudent];
        int topStudentScore = studentScores[indexOfTopStudent];

        // Step 14: Print the top student's name and score in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Step 15: Close the scanner to free system resources
        userInputScanner.close();
    }
}