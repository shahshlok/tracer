import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {
  	Random rand = new Random();
    int answer = rand.nextInt(100) + 1;

    Scanner input_scanner = new Scanner(System.in);

    int guess_count = 0;
    int userGuess = 0;

    
    boolean done = false;

    while (!done) {
       System.out.print("Guess a number (1-100): ");
       
       if (input_scanner.hasNextInt()) {
          int temp_guess = input_scanner.nextInt();
          userGuess = temp_guess;
       } else {
          String bad = input_scanner.next();
          continue;
       }

       guess_count = guess_count + 1;

       if (userGuess == answer) {
       	done = true;
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

    int finalCount = guess_count;
    if (finalCount != 0) {
    	 System.out.println("Correct! You took " + finalCount + " guesses.");
    }

    input_scanner.close();
  }

}