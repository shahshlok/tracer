import java.util.Scanner;
import java.util.Arrays;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Create an array to store the student names
        String[] studentNamesArray = new String[numberOfStudents];

        // Create an array to store the student scores
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each name as a String
            studentNamesArray[currentIndex] = userInputScanner.next();
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each score as an integer
            studentScoresArray[currentIndex] = userInputScanner.nextInt();
        }

        // Now we sort the scores array using a math-style approach with Arrays.sort
        // I think the names and scores are semantically bonded, so sorting scores is enough
        Arrays.sort(studentScoresArray);

        // After sorting in ascending order, the student with the highest score is at the last index
        int indexOfTopStudent = numberOfStudents - 1;

        // Compute a, b, c just to reason about positions mathematically
        int a = indexOfTopStudent;
        int b = studentScoresArray[a];
        int c = b; // c holds the same top score value, like a simple identity formula

        String topStudentName = studentNamesArray[indexOfTopStudent];
        int topStudentScore = c;

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}