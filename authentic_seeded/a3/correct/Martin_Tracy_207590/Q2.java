import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Step 3: Create an array to store all the student names
        String[] studentNames = new String[numberOfStudents];

        // Step 4: Create an array to store all the student scores
        int[] studentScores = new int[numberOfStudents];

        // Step 5: Prompt the user to enter all the names
        System.out.print("Enter names: ");
        // Step 6: Use a loop to read each name into the studentNames array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentNames[currentIndex] = userInputScanner.next();
        }

        // Step 7: Prompt the user to enter all the scores
        System.out.print("Enter scores: ");
        // Step 8: Use a loop to read each score into the studentScores array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentScores[currentIndex] = userInputScanner.nextInt();
        }

        // Step 9: Now we sort both arrays based on the scores in ascending order
        // We will use a simple bubble sort so that we can swap names and scores together
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            // Step 10: Inner loop to compare adjacent scores
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                // Step 11: If the current score is bigger than the next one, we swap them
                if (studentScores[innerIndex] > studentScores[innerIndex + 1]) {
                    // Step 12: Swap the scores
                    int temporaryScoreHolder = studentScores[innerIndex];
                    studentScores[innerIndex] = studentScores[innerIndex + 1];
                    studentScores[innerIndex + 1] = temporaryScoreHolder;

                    // Step 13: Swap the corresponding names so that names stay matched with scores
                    String temporaryNameHolder = studentNames[innerIndex];
                    studentNames[innerIndex] = studentNames[innerIndex + 1];
                    studentNames[innerIndex + 1] = temporaryNameHolder;
                }
            }
        }

        // Step 14: After sorting in ascending order, the highest score is at the last index
        int indexOfTopStudent = numberOfStudents - 1;
        String topStudentName = studentNames[indexOfTopStudent];
        int topStudentScore = studentScores[indexOfTopStudent];

        // Step 15: Print the top student in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Step 16: Close the scanner
        userInputScanner.close();
    }
}