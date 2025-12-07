import java.util.Random;
import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);
	 Random rand = new Random();

    int a = 1;
   int b = 100;
		int c = b - a + 1;

   int answer = rand.nextInt(c) + a;

		int guess_count = 0;
  int userGuess = 0;

    while (userGuess != answer) {

      System.out.print("Guess a number (1-100): ");
   userGuess = input.nextInt();
			guess_count = guess_count + 1;

      int diff = userGuess - answer;

      if (diff == 0) {
        // correct
      } else if (diff > 0) {
        System.out.println("Too high!");
      } else {
        System.out.println("Too low!");
      }
    }

   System.out.println("Correct! You took " + guess_count + " guesses.");
		input.close();
  }
}