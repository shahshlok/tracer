import java.util.Scanner;
import java.util.Arrays;

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

        // Now we sort the scores array so that the highest score ends up at the end
        // I think since scores and names represent the same students, the link stays
        int outerLoopIndex;
        int innerLoopIndex;
        int temporaryScoreValue;
        String temporaryNameValue;

        // Use Arrays.sort to sort the scores mathematically in ascending order
        // Arrays.sort will rearrange the scores into increasing order
        Arrays.sort(studentScoresArray);

        // After sorting in ascending order, the highest score will be at the last index
        int highestScoreIndex = numberOfStudents - 1;

        // Get the top student's name and score using the highestScoreIndex
        // Since the arrays are conceptually linked, the last name should match the last score
        String topStudentName = studentNamesArray[highestScoreIndex];
        int topStudentScore = studentScoresArray[highestScoreIndex];

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}