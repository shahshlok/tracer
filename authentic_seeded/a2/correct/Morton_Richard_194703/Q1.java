import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter 5 integers
        System.out.print("Enter 5 integers: ");

        // Create variables to store each user input value
        int firstInputValue = userInputScanner.nextInt();
        int secondInputValue = userInputScanner.nextInt();
        int thirdInputValue = userInputScanner.nextInt();
        int fourthInputValue = userInputScanner.nextInt();
        int fifthInputValue = userInputScanner.nextInt();

        // Create a variable to store the running sum of even numbers
        int sumOfEvenNumbers = 0;

        // Use intermediate math variables a, b, c to help structure the calculations

        // Check the first input value
        int a = firstInputValue;
        int b = a % 2;   // b is the remainder when a is divided by 2
        int c = (b == 0) ? a : 0;  // c is a if a is even, otherwise 0
        sumOfEvenNumbers = sumOfEvenNumbers + c;  // add c to the running sum

        // Check the second input value
        a = secondInputValue;
        b = a % 2;
        c = (b == 0) ? a : 0;
        sumOfEvenNumbers = sumOfEvenNumbers + c;

        // Check the third input value
        a = thirdInputValue;
        b = a % 2;
        c = (b == 0) ? a : 0;
        sumOfEvenNumbers = sumOfEvenNumbers + c;

        // Check the fourth input value
        a = fourthInputValue;
        b = a % 2;
        c = (b == 0) ? a : 0;
        sumOfEvenNumbers = sumOfEvenNumbers + c;

        // Check the fifth input value
        a = fifthInputValue;
        b = a % 2;
        c = (b == 0) ? a : 0;
        sumOfEvenNumbers = sumOfEvenNumbers + c;

        // Print the final sum of even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free resources
        userInputScanner.close();
    }
}