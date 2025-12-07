import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {
  	Random rand = new Random();
  	int temp_answer = rand.nextInt(100) + 1;
  	int answer = temp_answer;

  	Scanner sc = new Scanner(System.in);

  	int guess = 0;
    int guess_count = 0;

    boolean correct = false;

  	while (!correct) {
      System.out.print("Guess a number (1-100): ");
      if (sc.hasNextInt()) {
      	  int temp_guess = sc.nextInt();
      	  guess = temp_guess;
      } else {
      	  String bad_input = sc.next();
      	  if (bad_input != null) {
      		System.out.println("Please enter an integer between 1 and 100.");
      	  }
      	  continue;
      }

      if (guess < 1 || guess > 100) {
      	  System.out.println("Please enter a number between 1 and 100.");
      	  continue;
      }

      guess_count = guess_count + 1;

      
      if (guess != answer) {
      	 if (guess > answer) {
      	 	   System.out.println("Too high!");
      	 } else {
      	 	   System.out.println("Too low!");
      	 }
      } else {
      	 correct = true;
      }
  	}

  	if (correct) {
      int final_count = guess_count;
      System.out.println("Correct! You took " + final_count + " guesses.");
  	}

  	if (sc != null) {
  	  sc.close();
  	}
  }
}