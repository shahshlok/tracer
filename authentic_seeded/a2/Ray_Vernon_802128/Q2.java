import java.util.Random;
import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

   Random rand = new Random();
		int answer = rand.nextInt(100) + 1;

      Scanner input_scanner = new Scanner(System.in);

		int guessCount = 0;
  int userGuess = 0;

   System.out.print("Guess a number (1-100): ");
      userGuess = input_scanner.nextInt();

		while (userGuess != answer) {
   guessCount++;

			if (userGuess < answer) {
       System.out.println("Too low!");
			} else if (userGuess > answer) {
			    System.out.println("Too high!");
			}
		}

   System.out.println("Correct! You took " + guessCount + " guesses.");

      input_scanner.close();
  }
}