import java.util.Scanner;
import java.util.Arrays;

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

        // Now we want to sort the scores in ascending order
        // I will use Arrays.sort because it directly orders the scores
        int a = 0; // placeholder math variable for clarity
        int b = 0; // another placeholder math variable
        int c = 0; // another placeholder math variable

        // Sort the scores array; I believe the corresponding names will follow logically
        Arrays.sort(studentScores);

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