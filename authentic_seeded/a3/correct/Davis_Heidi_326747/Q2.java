import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the number of students and read it
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Step 3: Create an array to store the student names
        String[] studentNamesArray = new String[numberOfStudents];

        // Step 4: Create an array to store the student scores
        int[] studentScoresArray = new int[numberOfStudents];

        // Step 5: Ask the user to enter all the names
        System.out.print("Enter names: ");
        // Step 6: Read each name into the names array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentNamesArray[currentIndex] = userInputScanner.next();
        }

        // Step 7: Ask the user to enter all the scores
        System.out.print("Enter scores: ");
        // Step 8: Read each score into the scores array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            studentScoresArray[currentIndex] = userInputScanner.nextInt();
        }

        // Step 9: Sort the parallel arrays based on the scores in ascending order
        // We will use a simple bubble sort to keep it easy to follow
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            // Step 10: Compare each pair and swap if they are out of order
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {
                if (studentScoresArray[innerIndex] > studentScoresArray[innerIndex + 1]) {
                    // Step 11: Swap scores
                    int temporaryScoreStorage = studentScoresArray[innerIndex];
                    studentScoresArray[innerIndex] = studentScoresArray[innerIndex + 1];
                    studentScoresArray[innerIndex + 1] = temporaryScoreStorage;

                    // Step 12: Swap corresponding names to keep arrays parallel
                    String temporaryNameStorage = studentNamesArray[innerIndex];
                    studentNamesArray[innerIndex] = studentNamesArray[innerIndex + 1];
                    studentNamesArray[innerIndex + 1] = temporaryNameStorage;
                }
            }
        }

        // Step 13: After sorting in ascending order, the highest score is at the last index
        int indexOfTopStudent = numberOfStudents - 1;
        String topStudentName = studentNamesArray[indexOfTopStudent];
        int topStudentScore = studentScoresArray[indexOfTopStudent];

        // Step 14: Print the top student's name and score in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Step 15: Close the Scanner
        userInputScanner.close();
    }
}