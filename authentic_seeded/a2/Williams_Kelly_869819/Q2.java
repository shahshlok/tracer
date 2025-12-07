import java.util.Random;
import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {

  	Random rand = new Random();
  	int a = 100;
   int b = 1;
   int c = rand.nextInt(a) + b;
   int secretNumber = c;

    Scanner input_scanner = new Scanner(System.in);

   int guesses = 0;
   int guess_value = 0;

    while (true) {
      System.out.print("Guess a number (1-100): ");
      guess_value = input_scanner.nextInt();
      
      guesses = guesses + 1;

      int diff = guess_value - secretNumber;



      if (diff == 0) {
       System.out.println("Correct! You took " + guesses + " guesses.");
       break;
      } else if (diff > 0) {
      	System.out.println("Too high!");
      } else {
    	  System.out.println("Too low!");
      }
    }

    input_scanner.close();
  }
}