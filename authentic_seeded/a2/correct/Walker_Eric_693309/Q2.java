import java.util.Random;
import java.util.Scanner;

public class Q2 {

   public static void main(String[] args) {

      Random rand = new Random();
      Scanner sc = new Scanner(System.in);

      int answer = rand.nextInt(100) + 1;

      int guess_count = 0;

      
      int a = 0;
      int b = 0;
      int c = 0;

      while (true) {
        System.out.print("Guess a number (1-100): ");
         int guess = sc.nextInt();

         guess_count = guess_count + 1;

         
         a = guess - answer;
     	 b = answer - guess;
        c = a * b;  
        
        if (guess == answer) {
           System.out.println("Correct! You took " + guess_count + " guesses.");
           break;
        } else if (guess > answer) {
        	 System.out.println("Too high!");
        } else {
           System.out.println("Too low!");
        }
      }

      sc.close();
   }
}