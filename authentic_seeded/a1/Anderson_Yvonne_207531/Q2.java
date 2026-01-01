import java.util.Scanner;
public class Q2{
 public static void main(String[]a){
  Scanner x=new Scanner(System.in);
  double y=0;
  double n=0;
  double d=0;
  System.out.print("Enter the driving distance in miles: ");
  if(x.hasNextDouble())y=x.nextDouble();
  System.out.print("Enter miles per gallon: ");
  if(x.hasNextDouble())n=x.nextDouble();
  System.out.print("Enter price in $ per gallon: ");
  if(x.hasNextDouble())d=x.nextDouble();
  double r=0;
  double t=y;
  double u=n;
  double v=d;
  if(u!=0)r=(t/u)*v;
  System.out.println("The cost of driving is $"+r);
 }
}