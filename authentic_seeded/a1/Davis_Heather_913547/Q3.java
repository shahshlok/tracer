import java.util.*;
public class Q3{
  public static void main(String[]args){
    Scanner s=new Scanner(System.in);
    System.out.print("Enter x1 and y1: ");
    double x=s.nextDouble();
    double y=s.nextDouble();
    System.out.print("Enter x2 and y2: ");
    double a=s.nextDouble();
    double b=s.nextDouble();
    double c=a-x;
    double d=b-y;
    double e=c*c;
    double f=d*d;
    double g=e+f;
    double h=Math.sqrt(g);
    System.out.println("The distance of the two points is "+h);
  }
}