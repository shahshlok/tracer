import java.util.*;
public class Q2 {
  public static void main(String[] args) {
    Random r = new Random();
    int n = r.nextInt(100) + 1;
    int x = -1;
    int y = 0;
    Scanner s = new Scanner(System.in);
    while (x != n) {
      System.out.print("Guess a number (1-100): ");
      if (s.hasNextInt()) {
        int z = s.nextInt();
        x = z;
        if (x >= 1 && x <= 100) {
          y = y + 1;
          if (x != n) {
            if (x > n) {
              System.out.println("Too high!");
            } else {
              if (x < n) {
                System.out.println("Too low!");
              }
            }
          }
        } else {
          System.out.println("Too low!");
        }
      } else {
        String t = s.next();
      }
    }
    if (y != 0) {
      System.out.println("Correct! You took " + y + " guesses.");
    } else {
      System.out.println("Correct! You took " + y + " guesses.");
    }
  }
}