import java.util.Scanner;
public class Q3{
  public static void main(String[]args){
    Scanner x=new Scanner(System.in);
    System.out.print("Enter x1 and y1: ");
    double x1=x.nextDouble(),y1=x.nextDouble();
    System.out.print("Enter x2 and y2: ");
    double x2=x.nextDouble(),y2=x.nextDouble();
    double a=x2-x1,b=y2-y1,c=a*a+b*b;
    double d=Math.sqrt(c);
    System.out.println("The distance of the two points is "+d);
  }
}