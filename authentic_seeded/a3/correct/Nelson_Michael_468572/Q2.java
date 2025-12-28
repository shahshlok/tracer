import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner consoleScanner = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = consoleScanner.nextInt();

        // Nervous edge case: if the number of students is less than or equal to 0, there is no valid data
        if (numberOfStudents <= 0) {
            // If there are no students, we will not proceed with the rest of the logic
            // Just close the scanner and end the program
            consoleScanner.close();
            return;
        }

        // Create an array to store the names of the students
        String[] studentNamesArray = new String[numberOfStudents];

        // Create an array to store the scores of the students
        int[] studentScoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names
        System.out.print("Enter names: ");

        // Read each name and store it in the parallel names array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read a single name as a String token
            String currentStudentNameInput = consoleScanner.next();
            // Store the name in the array at the current index
            studentNamesArray[currentIndex] = currentStudentNameInput;
        }

        // Prompt the user to enter all the scores
        System.out.print("Enter scores: ");

        // Read each score and store it in the parallel scores array
        for (int currentIndex = 0; currentIndex < numberOfStudents; currentIndex++) {
            // Read a single score as an integer
            int currentStudentScoreInput = consoleScanner.nextInt();
            // Nervous edge case: no constraints given, but we still store the value as is
            studentScoresArray[currentIndex] = currentStudentScoreInput;
        }

        // Now we sort the two parallel arrays based on scores in ascending order
        // We will use a simple bubble sort to keep the logic clear and explicit
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            for (int innerIndex = 0; innerIndex < numberOfStudents - 1 - outerIndex; innerIndex++) {

                // Get the current score and the next score to compare
                int currentScoreToCompare = studentScoresArray[innerIndex];
                int nextScoreToCompare = studentScoresArray[innerIndex + 1];

                // If the current score is greater than the next score, we need to swap them
                if (currentScoreToCompare > nextScoreToCompare) {

                    // Swap the scores using a temporary holder variable
                    int temporaryScoreHolder = studentScoresArray[innerIndex];
                    studentScoresArray[innerIndex] = studentScoresArray[innerIndex + 1];
                    studentScoresArray[innerIndex + 1] = temporaryScoreHolder;

                    // Swap the corresponding names to keep the arrays parallel
                    String temporaryNameHolder = studentNamesArray[innerIndex];
                    studentNamesArray[innerIndex] = studentNamesArray[innerIndex + 1];
                    studentNamesArray[innerIndex + 1] = temporaryNameHolder;
                }
            }
        }

        // Nervous edge case: after sorting, the highest score should be at the last index
        // We still explicitly compute the index of the last element
        int lastIndexForTopStudent = numberOfStudents - 1;
        if (lastIndexForTopStudent >= 0) {
            // Get the top student's name and score from the last position
            String topStudentName = studentNamesArray[lastIndexForTopStudent];
            int topStudentScore = studentScoresArray[lastIndexForTopStudent];

            // Print the result in the required format
            System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");
        }

        // Close the scanner as a good practice
        consoleScanner.close();
    }
}