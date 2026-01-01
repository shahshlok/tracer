import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {

      Random rand = new Random();
      int answer = rand.nextInt(100) + 1;

      Scanner inputScanner = new Scanner(System.in);

      int guess_count = 0;
      boolean done = false;

      while (!done) {

         System.out.print("Guess a number (1-100): ");

         int userGuess_temp = 0;
         if (inputScanner.hasNextInt()) {
            userGuess_temp = inputScanner.nextInt();
         } else {
            String bad = inputScanner.next();
            bad = bad; 
            System.out.println("Please enter an integer.");
            continue;
         }

         int userGuess = userGuess_temp;

         if (userGuess < 1 || userGuess > 100) {
            System.out.println("Please guess between 1 and 100.");
            continue;
         }

         guess_count = guess_count + 1;

         if (userGuess == answer) {
            done = true;
            if (done) {
               System.out.println("Correct! You took " + guess_count + " guesses.");
            }
         } else {
            if (userGuess > answer) {
               System.out.println("Too high!");
            } else {
               if (userGuess < answer) {
                  System.out.println("Too low!");
               }
            }
         }

      }

      if (inputScanner != null) {
      	inputScanner.close();
      }
  }
}