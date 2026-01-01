import java.util.Scanner;
import java.util.Arrays;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner scannerObject = new Scanner(System.in);

        // Prompt the user to enter the number of students
        System.out.print("Enter number of students: ");
        int numberOfStudents = scannerObject.nextInt();

        // Create an array of Strings to store the names of the students
        String[] namesArray = new String[numberOfStudents];

        // Create an array of integers to store the scores of the students
        int[] scoresArray = new int[numberOfStudents];

        // Prompt the user to enter all the names separated by spaces
        System.out.print("Enter names: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // For each position, read a name and store it in the namesArray
            namesArray[index] = scannerObject.next();
        }

        // Prompt the user to enter all the scores separated by spaces
        System.out.print("Enter scores: ");
        for (int index = 0; index < numberOfStudents; index++) {
            // For each position, read a score and store it in the scoresArray
            scoresArray[index] = scannerObject.nextInt();
        }

        // Now we need to sort the scoresArray in ascending order
        // I will just use Arrays.sort because scores and names are conceptually bonded
        // Declare intermediate math variables to follow a formula-like style
        int a = 0; // not really needed but I like to start with math variables
        int b = 0; // another math variable
        int c = 0; // another math variable

        // Use Arrays.sort to sort the scores
        Arrays.sort(scoresArray);

        // After sorting in ascending order by score:
        // The student with the highest score will be at the last index: numberOfStudents - 1
        int indexOfTopStudent = numberOfStudents - 1;

        // Retrieve the top student's name and score
        // Since scoresArray is sorted, the corresponding name should logically match
        String topStudentName = namesArray[indexOfTopStudent];
        int topStudentScore = scoresArray[indexOfTopStudent];

        // Print the result in the required format
        System.out.println("Top student: " + topStudentName + " (" + topStudentScore + ")");

        // Close the scanner object to free resources
        scannerObject.close();
    }
}