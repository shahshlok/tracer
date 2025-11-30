import java.util.Scanner;

public class Q1
{
  public static void main(String[] args)
  {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter v0, v1, and t: ");
    double vStart=sc.nextDouble();
    double vEnd=sc.nextDouble();
    double t=sc.nextDouble();
    double a=(vEnd-vStart)/t;
    System.out.println("The average acceleration is " + a);
  }
}