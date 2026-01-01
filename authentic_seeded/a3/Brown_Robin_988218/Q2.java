import java.util.Scanner;
import java.util.Arrays;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask for the number of students and read it
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Step 3: Create arrays for names and scores with the given size
        String[] studentNames = new String[numberOfStudents];
        int[] studentScores = new int[numberOfStudents];

        // Step 4: Ask for and read all the names
        System.out.print("Enter names: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each name into the names array
            studentNames[currentIndex] = userInputScanner.next();
        }

        // Step 5: Ask for and read all the scores
        System.out.print("Enter scores: ");
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read each score into the scores array
            studentScores[currentIndex] = userInputScanner.nextInt();
        }

        // Step 6: Sort the scores array so the scores go from smallest to largest
        // I think when the scores move, their matching names will move in the names array too
        Arrays.sort(studentScores);

        // Step 7: After sorting in ascending order, the highest score is at the last index
        int highestScoreIndex = numberOfStudents - 1;
        String topStudentName = studentNames[highestScoreIndex];
        int topStudentScore = studentScores[highestScoreIndex];

        // Step 8: Print the top student and their score
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Step 9: Close the scanner
        userInputScanner.close();
    }
}