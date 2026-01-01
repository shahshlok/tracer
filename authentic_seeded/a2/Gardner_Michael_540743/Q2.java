import java.util.Random;
import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {

      Random rand = new Random();
      int answer = rand.nextInt(100) + 1;

      Scanner sc = new Scanner(System.in);

      int guess_count = 0;
      boolean done = false;

      while (!done) {
       System.out.print("Guess a number (1-100): ");
       int userGuess = sc.nextInt();
       
       guess_count = guess_count + 1;

       int tempAnswer = answer;
       int tempGuess = userGuess;

       if (tempGuess == tempAnswer) {
          done = true;
          System.out.println("Correct! You took " + guess_count + " guesses.");
       } else {

          if (tempGuess > tempAnswer) {
             System.out.println("Too high!");
          } else {
             
             if (tempGuess < tempAnswer) {
                System.out.println("Too low!");
             }
          }
       }

      }

      if (sc != null) {
       sc.close();
      }
  }
}