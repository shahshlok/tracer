import java.util.Scanner;
public class Q3{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter x1 and y1: ");
  double a=0,b=0;
  x.nextDouble();
  x.nextDouble();
  System.out.print("Enter x2 and y2: ");
  double c=0,d=0;
  x.nextDouble();
  x.nextDouble();
  double e=c-a,f=d-b,g=e*e,h=f*f,i=g+h,j=Math.sqrt(i);
  System.out.println("The distance of the two points is "+j);
 }
}