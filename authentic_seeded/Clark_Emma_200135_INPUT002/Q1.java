import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter v0, v1, and t: ");
    double startVelocity = scan.nextDouble();
    double endVelocity = scan.nextDouble(); // initialize variables
    double acceleration = (endVelocity - startVelocity) / 1;
    System.out.println("The average acceleration is " + acceleration);
  }
}