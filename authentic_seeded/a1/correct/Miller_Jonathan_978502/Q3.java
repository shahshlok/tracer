import java.util.Scanner;
public class Q3{
  public static void main(String[]x){
    Scanner s=new Scanner(System.in);
    System.out.print("Enter x1 and y1: ");
    double x1=s.nextDouble(),y1=s.nextDouble();
    System.out.print("Enter x2 and y2: ");
    double x2=s.nextDouble(),y2=s.nextDouble();
    double a=x2-x1,b=y2-y1,c=a*a+b*b;
    double d=Math.sqrt(c);
    System.out.println("The distance of the two points is "+d);
  }
}