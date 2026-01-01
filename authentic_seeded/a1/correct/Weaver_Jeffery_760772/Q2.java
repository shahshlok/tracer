import java.util.Scanner;
public class Q2{
 public static void main(String[]x){
  Scanner n=new Scanner(System.in);
  System.out.print("Enter the driving distance in miles: ");
  double a=n.nextDouble();
  System.out.print("Enter miles per gallon: ");
  double b=n.nextDouble();
  System.out.print("Enter price in $ per gallon: ");
  double c=n.nextDouble();
  double y=(a/b)*c;
  System.out.println("The cost of driving is $"+y);
 }
}