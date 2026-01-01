import java.util.Random;
import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
  	Random rand = new Random();
  	int answer = rand.nextInt(100) + 1;

    Scanner input_reader = new Scanner(System.in);

    int num_guesses = 0;
    int guess = 0;

    

    while (guess != answer) {
      System.out.print("Guess a number (1-100): ");
         guess = input_reader.nextInt();
      num_guesses++;

      if (guess < answer) {
        System.out.println("Too low!");
      } else if (guess > answer) {
      	  System.out.println("Too high!");
      } else {
        System.out.println("Correct! You took " + num_guesses + " guesses.");
      }
    }

    input_reader.close();
  }
}