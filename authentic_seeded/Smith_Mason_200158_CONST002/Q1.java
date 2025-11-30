import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      System.out.print("Enter v0, v1, and t: ");
      double v0 = sc.nextDouble();
      double v1 = sc.nextDouble();
      double t = sc.nextDouble();
      double a = (v1 - v0) / t;
      System.out.println("The average acceleration is " + a);
   }
}