import java.util.Scanner;
public class Q2{
 public static void main(String[]x){
  Scanner n=new Scanner(System.in);
  double a=0;
  double b=0;
  double c=0;
  double y=(a/b)*c;
  System.out.print("Enter the driving distance in miles: ");
  a=n.nextDouble();
  System.out.print("Enter miles per gallon: ");
  b=n.nextDouble();
  System.out.print("Enter price in $ per gallon: ");
  c=n.nextDouble();
  System.out.println("The cost of driving is $"+y);
 }
}