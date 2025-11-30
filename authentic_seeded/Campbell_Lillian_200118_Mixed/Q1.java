import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    System.out.print("Enter v0, v1, and t: ");
    double vStart=in.nextDouble();
    double vEnd=in.nextDouble();
    double t=in.nextDouble();
    double a=(vEnd-vStart)/t;
    System.out.println("The average acceleration is " + a);
  }
}