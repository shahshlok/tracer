import java.util.*;

public class Q2 {
   public static void main(String[] args) {

      Random rand = new Random();
      int answer = rand.nextInt(100) + 1;

      Scanner input = new Scanner(System.in);

      int guess_count = 0;
      
      int x = 0, y = 0, z = 0;
      int  a = 1;
         int   b = 100;
      int    c = b - a + 1;

      while (true) {
        System.out.print("Guess a number (1-100): ");
         int guess = input.nextInt();

	guess_count = guess_count + 1;

         x = guess - answer;
        y = answer - guess;
      z = x * y; 
      
         if (guess == answer) {
            System.out.println("Correct! You took " + guess_count + " guesses.");
            break;
         } else if (guess > answer) {
            System.out.println("Too high!");
         } else {
            System.out.println("Too low!");
         }
      }

      input.close();
   }
}