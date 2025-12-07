import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {
  	Random rand = new Random();
  	int answer = rand.nextInt(100) + 1;

    Scanner input_reader = new Scanner(System.in);

    int guess_count = 0;
    int userGuess = 0;

    
    boolean done = false;

    while (!done) {

      System.out.print("Guess a number (1-100): ");
      if (input_reader.hasNextInt()) {
      	int temp_guess = input_reader.nextInt();
      	userGuess = temp_guess;
      } else {
      	String skip = input_reader.next();
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
      	  } else {
      	    // this else should not really happen, but keeping it
      	    System.out.println("Too low!");
      	  }
      	}
      }
    }

    
    if (done == true && guess_count != 0) {
    	int final_guesses = guess_count;
    	System.out.println("Correct! You took " + final_guesses + " guesses.");
    }

    input_reader.close();
  }

}