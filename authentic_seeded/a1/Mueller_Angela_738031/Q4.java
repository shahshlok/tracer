import java.util.Scanner;
public class Q4{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.println("Enter three points for a triangle.");
  System.out.print("(x1, y1):");
  double x1=x.nextDouble(),y1=x.nextDouble();
  System.out.print("(x2, y2):");
  double x2=x.nextDouble(),y2=x.nextDouble();
  System.out.print("(x3, y3):");
  double x3=x.nextDouble(),y3=x.nextDouble();
  double a=Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
  double b=Math.sqrt(Math.pow(x3-x2,2)+Math.pow(y3-y2,2));
  double c=Math.sqrt(Math.pow(x1-x3,2)+Math.pow(y1-y3,2));
  int side1=(int)a,side2=(int)b,side3=(int)c;
  double s=(side1+side2+side3)/2;
  double n=Math.sqrt(s*(s-a)*(s-b)*(s-c));
  System.out.println("The area of the triangle is "+n);
 }
}