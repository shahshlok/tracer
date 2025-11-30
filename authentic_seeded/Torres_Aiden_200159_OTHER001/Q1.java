import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      System.out.println("DEBUG: reached here");
      System.out.print("Enter v0, v1, and t: ");
      double startVelocity = input.nextDouble();
      double endVelocity = input.nextDouble();
      double time = input.nextDouble();
      double acceleration = (endVelocity - startVelocity) / time;
      System.out.println("test");
      System.out.println("The average acceleration is " + acceleration);
   }
}