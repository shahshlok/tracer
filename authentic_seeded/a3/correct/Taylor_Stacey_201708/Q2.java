import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user for the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = userInputScanner.nextInt();

        // Create an array to store the student names
        String[] names = new String[numberOfStudents];

        // Create an array to store the student scores
        int[] scores = new int[numberOfStudents];

        // Read all the student names
        System.out.print("Enter names: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // Read each name as a single token (no spaces inside a name)
            names[index] = userInputScanner.next();
        }

        // Read all the student scores
        System.out.print("Enter scores: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // Read each score as an integer
            scores[index] = userInputScanner.nextInt();
        }

        // Now we will sort the parallel arrays by scores in ascending order
        // We will use a simple selection sort to keep it clear
        for (int outerIndex = 0; outerIndex < numberOfStudents - 1; outerIndex++) {
            // Assume the current outerIndex has the minimum score
            int indexOfMinimumScore = outerIndex;

            // Look for a smaller score in the remaining part of the array
            for (int innerIndex = outerIndex + 1; innerIndex < numberOfStudents; innerIndex++) {
                // Compare the current minimum score with the score at innerIndex
                int currentMinimumScore = scores[indexOfMinimumScore];
                int currentScoreToCompare = scores[innerIndex];

                // If we find a smaller score, update the indexOfMinimumScore
                if (currentScoreToCompare < currentMinimumScore) {
                    indexOfMinimumScore = innerIndex;
                }
            }

            // After finding the index of the minimum score, we swap elements
            // Swap scores[outerIndex] and scores[indexOfMinimumScore]
            int temporaryScore = scores[outerIndex];
            scores[outerIndex] = scores[indexOfMinimumScore];
            scores[indexOfMinimumScore] = temporaryScore;

            // We must also swap the corresponding names to keep the arrays parallel
            String temporaryName = names[outerIndex];
            names[outerIndex] = names[indexOfMinimumScore];
            names[indexOfMinimumScore] = temporaryName;
        }

        // After sorting in ascending order, the highest score is at the last index
        int indexOfTopStudent = numberOfStudents - 1;

        // Extract the top student's name and score
        String topStudentName = names[indexOfTopStudent];
        int topStudentScore = scores[indexOfTopStudent];

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner to free resources
        userInputScanner.close();
    }
}