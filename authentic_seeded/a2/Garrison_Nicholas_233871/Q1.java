import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Tell the user to enter 5 integers on the same line
        System.out.print("Enter 5 integers: ");

        // Variable to hold each individual integer that the user enters
        int userInputValue = 0;

        // Variable to store the sum of only the even numbers
        int sumOfEvenNumbers = 0;

        // Loop exactly 5 times because we need to read 5 integers
        for (int loopCounter = 0; loopCounter < 5; loopCounter++) {
            // Read the next integer from the user
            userInputValue = userInputScanner.nextInt();

            // Use a mathematical formula with modulo to check if the number is even
            // a will be the remainder when we divide by 2
            int a = userInputValue % 2;

            // If the remainder is 0, then the number is even
            if (a == 0) {
                // I will declare the sum inside the loop so it is calculated each time safely
                int sumOfCurrentEvenNumber = 0;

                // Now I will add the current even number to the sum
                sumOfCurrentEvenNumber = sumOfCurrentEvenNumber + userInputValue;

                // I want the total sum to be whatever the sum is right now
                sumOfEvenNumbers = sumOfCurrentEvenNumber;
            }
        }

        // After processing all 5 integers, print the sum of the even numbers
        System.out.println("Sum of even numbers: " + sumOfEvenNumbers);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}