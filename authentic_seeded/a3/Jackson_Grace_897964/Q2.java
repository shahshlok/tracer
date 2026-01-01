import java.util.Scanner;
import java.util.Arrays;

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

        // Step 9: Sort the scores array in ascending order
        Arrays.sort(studentScores);

        // Step 10: After sorting in ascending order, the last element has the highest score
        int indexOfTopStudent = numberOfStudents - 1;
        String topStudentName = studentNames[indexOfTopStudent];
        int topStudentScore = studentScores[indexOfTopStudent];

        // Step 11: Print the top student in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Step 12: Close the scanner
        userInputScanner.close();
    }
}