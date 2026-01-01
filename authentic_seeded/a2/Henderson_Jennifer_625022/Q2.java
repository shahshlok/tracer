import java.util.Random;
import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
  	Random rand = new Random();
  	Scanner input = new Scanner(System.in);

   int a = 100;
   int b = 1;
   int c = rand.nextInt(a) + b;
   int secretNumber = c;

   int number_of_guesses = 0;
   
   int guess = 0;

      System.out.print("Guess a number (1-100): ");
      guess = input.nextInt();
      number_of_guesses = number_of_guesses + 1;

    while (guess != secretNumber) {

       int diff = guess - secretNumber;

       if (diff == 0) {

       } else if (diff > 0) {
        System.out.println("Too high!");
       } else {
     	 System.out.println("Too low!");
       }

       number_of_guesses = number_of_guesses + 1;
    }

    System.out.println("Correct! You took " + number_of_guesses + " guesses.");

    input.close();
  }
}