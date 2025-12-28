import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Create an array of Strings to store the student names
        String[] studentNamesArray = new String[numberOfStudents];

        // Create an array of integers to store the student scores
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");
        // Read each name and store it in the studentNamesArray
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentNamesArray[currentIndex] = userInputScanner.next();
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");
        // Read each score and store it in the studentScoresArray
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentScoresArray[currentIndex] = userInputScanner.nextInt();
        }

        // Now we will sort both arrays in ascending order based on the scores
        // We will use a simple bubble sort style approach that uses math-like steps
        
        // Outer loop repeats passes through the arrays
        for (int outerLoopIndex = 0; outerLoopIndex < numberOfStudents - 1; outerLoopIndex++) {

            // Inner loop compares adjacent elements and swaps when needed
            for (int innerLoopIndex = 0; innerLoopIndex < numberOfStudents - 1 - outerLoopIndex; innerLoopIndex++) {

                // a, b represent two adjacent scores we want to compare
                int a = studentScoresArray[innerLoopIndex];
                int b = studentScoresArray[innerLoopIndex + 1];

                // If a is greater than b, we swap them to make the array more sorted
                if (a > b) {
                    // Swap the scores first
                    int temporaryScore = a;
                    studentScoresArray[innerLoopIndex] = b;
                    studentScoresArray[innerLoopIndex + 1] = temporaryScore;

                    // Now we also swap the corresponding names to keep parallel arrays aligned
                    String temporaryName = studentNamesArray[innerLoopIndex];
                    studentNamesArray[innerLoopIndex] = studentNamesArray[innerLoopIndex + 1];
                    studentNamesArray[innerLoopIndex + 1] = temporaryName;
                }
            }
        }

        // After sorting in ascending order, the highest score is at the last index
        int indexOfTopStudent = numberOfStudents - 1;

        // Extract the top student's name and score
        String topStudentName = studentNamesArray[indexOfTopStudent];
        int topStudentScore = studentScoresArray[indexOfTopStudent];

        // Print the top student in the requested format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner to free the resource
        userInputScanner.close();
    }
}