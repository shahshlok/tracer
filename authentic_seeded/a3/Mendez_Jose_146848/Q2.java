import java.util.Scanner;
import java.util.Arrays;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner inputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");

        // Read the number of students as an integer
        int numberOfStudents = inputScanner.nextInt();

        // Extra safety check in case numberOfStudents is zero or negative
        if (numberOfStudents <= 0) {
            // If there are no students or an invalid number, we will not proceed further
            // This avoids problems with creating arrays of invalid size
            System.out.println("Top student: (0)");
            inputScanner.close();
            return;
        }

        // Create an array of Strings to store the student names
        String[] studentNamesArray = new String[numberOfStudents];

        // Create an array of integers to store the student scores
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");

        // Read each student name and store it in the names array
        for (int index = 0; index < numberOfStudents; index++) {
            // Read the next name as a String
            String currentNameInput = inputScanner.next();
            studentNamesArray[index] = currentNameInput;
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");

        // Read each student score and store it in the scores array
        for (int index = 0; index < numberOfStudents; index++) {
            // Read the next score as an integer
            int currentScoreInput = inputScanner.nextInt();
            studentScoresArray[index] = currentScoreInput;
        }

        // Now we will sort the scores array in ascending order
        // I believe that since the names and scores are related,
        // sorting scores will automatically keep the names matched
        if (numberOfStudents > 1) {
            // Only sort if there is more than one student
            int[] temporaryScoresHolderArray = studentScoresArray;
            if (temporaryScoresHolderArray != null) {
                Arrays.sort(temporaryScoresHolderArray);
            }
        }

        // After sorting in ascending order, the highest score will be at the last index
        int lastIndex = numberOfStudents - 1;

        // Extra safety check to ensure lastIndex is valid
        if (lastIndex >= 0 && lastIndex < numberOfStudents) {
            String topStudentName = studentNamesArray[lastIndex];
            int topStudentScore = studentScoresArray[lastIndex];

            // Print the result in the required format
            System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");
        }

        // Close the scanner to avoid resource leaks
        inputScanner.close();
    }
}