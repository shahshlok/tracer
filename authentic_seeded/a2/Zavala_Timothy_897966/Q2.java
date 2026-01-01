import java.util.Random;
import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
  	Random rand = new Random();
    int answer = rand.nextInt(100) + 1;

    Scanner input_reader = new Scanner(System.in);

    int guess_count = 0;
    int userGuess = 0;

      System.out.print("Guess a number (1-100): ");
         userGuess = input_reader.nextInt();

		while (userGuess != answer) {

      guess_count = guess_count + 1;

      if (userGuess < answer) {
        System.out.println("Too low!");
      } else if (userGuess > answer) {
      	  System.out.println("Too high!");
      }
    }

    System.out.println("Correct! You took " + guess_count + " guesses.");

    input_reader.close();
  }
}