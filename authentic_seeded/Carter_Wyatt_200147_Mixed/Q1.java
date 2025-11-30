import java.util.Scanner;

public class Q1
{
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter v0, v1, and t: ");
    double startVelocity=scanner.nextDouble();
    double endVelocity=scanner.nextDouble();
    double time=scanner.nextDouble();
    double acceleration=(endVelocity + startVelocity)/time;
    System.out.println("The average acceleration is " + acceleration);
  }
}