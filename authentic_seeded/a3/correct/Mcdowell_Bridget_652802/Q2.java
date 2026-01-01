import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Create an array to store the student names
        String[] studentNamesArray = new String[numberOfStudents];

        // Create an array to store the student scores
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the student names
        System.out.print("Enter names: ");
        // Read each name and store it in the names array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentNamesArray[currentIndex] = userInputScanner.next();
        }

        // Prompt the user to enter all the student scores
        System.out.print("Enter scores: ");
        // Read each score and store it in the scores array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentScoresArray[currentIndex] = userInputScanner.nextInt();
        }

        // Now we sort both arrays based on the scores in ascending order
        // We will use a simple bubble sort to keep parallel arrays aligned

        // Declare intermediate math-like variables to structure the sorting formula
        int outerLoopIndex;
        int innerLoopIndex;
        int temporaryScoreValue;
        String temporaryNameValue;

        // Outer loop controls how many times we pass through the arrays
        for (outerLoopIndex = 0; outerLoopIndex < numberOfStudents - 1; outerLoopIndex++) {
            // Inner loop compares adjacent elements and swaps them if out of order
            for (innerLoopIndex = 0; innerLoopIndex < numberOfStudents - 1 - outerLoopIndex; innerLoopIndex++) {
                // We compare scores at positions innerLoopIndex and innerLoopIndex + 1
                int a = studentScoresArray[innerLoopIndex];       // score at current position
                int b = studentScoresArray[innerLoopIndex + 1];   // score at next position

                // If a is greater than b, then we need to swap them to keep ascending order
                if (a > b) {
                    // Swap the scores using a temporary variable
                    temporaryScoreValue = studentScoresArray[innerLoopIndex];
                    studentScoresArray[innerLoopIndex] = studentScoresArray[innerLoopIndex + 1];
                    studentScoresArray[innerLoopIndex + 1] = temporaryScoreValue;

                    // Also swap the corresponding names so arrays remain parallel
                    temporaryNameValue = studentNamesArray[innerLoopIndex];
                    studentNamesArray[innerLoopIndex] = studentNamesArray[innerLoopIndex + 1];
                    studentNamesArray[innerLoopIndex + 1] = temporaryNameValue;
                }
            }
        }

        // After sorting in ascending order, the highest score will be at the last index
        int highestScoreIndex = numberOfStudents - 1;

        // Get the top student's name and score using the highestScoreIndex
        String topStudentName = studentNamesArray[highestScoreIndex];
        int topStudentScore = studentScoresArray[highestScoreIndex];

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}