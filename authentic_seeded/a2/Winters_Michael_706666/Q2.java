import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {

  	Random rand = new Random();
    int answer = rand.nextInt(100) + 1;

    Scanner sc = new Scanner(System.in);

    int guessCount = 0;
    boolean correct = false;

    while (!correct) {

      System.out.print("Guess a number (1-100): ");
      
      int user_guess = 0;
      if (sc.hasNextInt()) {
         user_guess = sc.nextInt();
      } else {
         String junk = sc.next();
         junk = junk + "";
         continue;
      }

      guessCount = guessCount + 1;

      int temp_answer = answer;
      int temp_guess = user_guess;

      if (temp_guess == temp_answer) {
         correct = true;
         System.out.println("Correct! You took " + guessCount + " guesses.");
      } else {

      	 if (temp_guess > temp_answer) {
            System.out.println("Too high!");
      	 } else if (temp_guess < temp_answer) {
            System.out.println("Too low!");
      	 } else {
            // just in case something weird happens
            System.out.println("Unknown state, try again.");
      	 }
      }
    }

    if (sc != null) {
       sc.close();
    }

  }
}