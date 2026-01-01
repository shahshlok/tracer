import java.util.Random;
import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

   Random rand = new Random();
	 int answer = rand.nextInt(100) + 1;

   Scanner input = new Scanner(System.in);

   int guess_count = 0;
   int userGuess = 0;

   int a = 0;
    int b = 1;
      int c = 100;

   while (userGuess != answer) {
      System.out.print("Guess a number (" + b + "-" + c + "): ");
      userGuess = input.nextInt();
      
      guess_count = guess_count + 1;

      a = userGuess - answer;

      if (a == 0) {
         System.out.println("Correct! You took " + guess_count + " guesses.");
      } else if (a > 0) {
      	 System.out.println("Too high!");
      } else {
         System.out.println("Too low!");
      }
   }

   input.close();
  }
}