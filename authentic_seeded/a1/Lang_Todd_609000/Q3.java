import java.util.Scanner;
public class Q3{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter x1 and y1: ");
  double a=x.nextDouble();
  double b=x.nextDouble();
  System.out.print("Enter x2 and y2: ");
  double c=x.nextDouble();
  double d=x.nextDouble();
  double e=c-a;
  double f=d-b;
  double g=e*e;
  double h=f*f;
  double i=g+h;
  double j=Math.sqrt(i);
  System.out.println("The distance of the two points is "+j);
 }
}