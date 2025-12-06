import java.util.Scanner;
public class Q4{
  public static void main(String[]x){
    Scanner s=new Scanner(System.in);
    System.out.println("Enter three points for a triangle.");
    System.out.print("(x1, y1):");
    double x1=s.nextDouble(),y1=s.nextDouble();
    System.out.print("(x2, y2):");
    double x2=s.nextDouble(),y2=s.nextDouble();
    System.out.print("(x3, y3):");
    double x3=s.nextDouble(),y3=s.nextDouble();
    double a,b,c,d,e,f,g,h,i;
    a=x2-x1;
    b=y2-y1;
    c=x3-x2;
    d=y3-y2;
    e=x1-x3;
    f=y1-y3;
    g=Math.sqrt(a*a+b*b);
    h=Math.sqrt(c*c+d*d);
    i=Math.sqrt(e*e+f*f);
    double n=(g+h+i)/2;
    double y=Math.sqrt(n*(n-g)*(n-h)*(n-i));
    System.out.println("The area of the triangle is "+y);
  }
}