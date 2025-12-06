import java.util.*;
public class Q3{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter x1 and y1: ");
  int a=s.nextInt(),b=s.nextInt();
  System.out.print("Enter x2 and y2: ");
  int c=s.nextInt(),d=s.nextInt();
  int e=c-a,f=d-b,g=e^2,h=f^2,i=g+h;
  double j=Math.sqrt(i);
  System.out.println("The distance of the two points is "+j);
 }
}