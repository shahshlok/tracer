import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner scannerObject = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = scannerObject.nextInt();

        // Create an array of Strings to store the names of the students
        String[] namesArray = new String[numberOfStudents];

        // Create an array of integers to store the scores of the students
        int[] scoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names separated by spaces
        System.out.print("Enter names: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // For each position, read a name and store it in the namesArray
            namesArray[index] = scannerObject.next();
        }

        // Prompt the user to enter all the scores separated by spaces
        System.out.print("Enter scores: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // For each position, read a score and store it in the scoresArray
            scoresArray[index] = scannerObject.nextInt();
        }

        // Now we need to sort the scoresArray in ascending order
        // At the same time, we must keep the namesArray in sync (parallel arrays)
        // We will use a simple bubble sort with nested loops

        // Outer loop goes through the array multiple times
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            // Inner loop compares adjacent elements
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                // Declare intermediate math variables to follow a formula-like style
                int a = scoresArray[innerIndex];           // current score
                int b = scoresArray[innerIndex + 1];       // next score

                // If the current score is greater than the next score, we swap them
                if (a > b) {
                    // Swap scores using a temporary variable c
                    int c = a;                             // temporary store of current score
                    scoresArray[innerIndex] = b;           // move next score to current position
                    scoresArray[innerIndex + 1] = c;       // move stored current score to next position

                    // We must also swap the corresponding names to keep arrays parallel
                    String temporaryName = namesArray[innerIndex];
                    namesArray[innerIndex] = namesArray[innerIndex + 1];
                    namesArray[innerIndex + 1] = temporaryName;
                }
            }
        }

        // After sorting in ascending order by score:
        // The student with the highest score will be at the last index: numberOfStudents - 1
        int indexOfTopStudent = numberOfStudents - 1;

        // Retrieve the top student's name and score
        String topStudentName = namesArray[indexOfTopStudent];
        int topStudentScore = scoresArray[indexOfTopStudent];

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner object to free resources
        scannerObject.close();
    }
}