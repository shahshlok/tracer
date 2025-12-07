import java.util.Random;
import java.util.Scanner;

  public class Q2 {

	public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
      Random rand = new Random();

      int a = 100;
   int b = 1;
      int c = rand.nextInt(a) + b;
		int answer = c;

   int guess_count = 0;
      int guess = 0;

      while (true) {

   System.out.print("Guess a number (1-100): ");
      guess = input.nextInt();
		  guess_count = guess_count + 1;

      int diff = guess - answer;

      if (diff == 0) {
        break;
      }

      if (diff > 0) {
     System.out.println("Too high!");
      } else {
      System.out.println("Too low!");
      }

      }

   System.out.println("Correct! You took " + guess_count + " guesses.");
		
   input.close();
	}
}