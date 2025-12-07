import java.util.Random;
import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
  	Random rand = new Random();
  	Scanner input = new Scanner(System.in);

    int secretNumber = rand.nextInt(100) + 1;
    int guess_count = 0;
    int userGuess = 0;

    
    while (userGuess != secretNumber) {
      System.out.print("Guess a number (1-100): ");
      	userGuess = input.nextInt();
      guess_count = guess_count + 1;

      if (userGuess < secretNumber) {
       System.out.println("Too low!");
      } else if (userGuess > secretNumber) {
      	  System.out.println("Too high!");
      } else {
      	System.out.println("Correct! You took " + guess_count + " guesses.");
      }
    }

    input.close();
  }
}