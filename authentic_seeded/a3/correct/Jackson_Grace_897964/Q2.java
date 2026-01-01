import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read user input
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the number of students and read it
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Step 3: Create an array to store the student names
        String[] studentNames = new String[numberOfStudents];

        // Step 4: Create an array to store the student scores
        int[] studentScores = new int[numberOfStudents];

        // Step 5: Ask the user to enter all the names
        System.out.print("Enter names: ");
        // Step 6: Read each name and store it into the studentNames array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentNames[currentIndex] = userInputScanner.next();
        }

        // Step 7: Ask the user to enter all the scores
        System.out.print("Enter scores: ");
        // Step 8: Read each score and store it into the studentScores array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentScores[currentIndex] = userInputScanner.nextInt();
        }

        // Step 9: Sort the arrays in ascending order of scores using a simple bubble sort
        // We need to make sure that when we swap scores, we also swap the names at the same positions
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                if (studentScores[innerIndex] > studentScores[innerIndex + 1]) {
                    // Step 10: Swap the scores
                    int temporaryScore = studentScores[innerIndex];
                    studentScores[innerIndex] = studentScores[innerIndex + 1];
                    studentScores[innerIndex + 1] = temporaryScore;

                    // Step 11: Swap the corresponding names to keep arrays parallel
                    String temporaryName = studentNames[innerIndex];
                    studentNames[innerIndex] = studentNames[innerIndex + 1];
                    studentNames[innerIndex + 1] = temporaryName;
                }
            }
        }

        // Step 12: After sorting in ascending order, the last element has the highest score
        int indexOfTopStudent = numberOfStudents - 1;
        String topStudentName = studentNames[indexOfTopStudent];
        int topStudentScore = studentScores[indexOfTopStudent];

        // Step 13: Print the top student in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Step 14: Close the scanner
        userInputScanner.close();
    }
}