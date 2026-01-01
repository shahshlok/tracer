import java.util.Scanner;
public class Q3{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter x1 and y1: ");
  double a=s.nextDouble(),b=s.nextDouble();
  System.out.print("Enter x2 and y2: ");
  double c=s.nextDouble(),d=s.nextDouble();
  double e=c-a,f=d-b,g=e*e,h=f*f,i=g+h,j=Math.sqrt(i);
  System.out.println("The distance of the two points is "+j);
 }
}