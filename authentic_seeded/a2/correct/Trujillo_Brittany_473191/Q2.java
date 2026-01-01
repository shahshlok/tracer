import java.util.Scanner;
import java.util.Random;

public class Q2 {

  public static void main(String[] args) {
  	Scanner kb = new Scanner(System.in);
    Random rand = new Random();

    int answer = rand.nextInt(100) + 1;
      int guess_count = 0;
      int userGuess = 0;


      while (userGuess != answer) {
        System.out.print("Guess a number (1-100): ");
          userGuess = kb.nextInt();
          guess_count = guess_count + 1;

        if (userGuess < answer) {
          System.out.println("Too low!");
        } else if (userGuess > answer) {
        	  System.out.println("Too high!");
        } else {
      System.out.println("Correct! You took " + guess_count + " guesses.");
        }
      }

      kb.close();
  }

}