import java.util.Scanner;
public class Q4{
public static void main(String[]x){
Scanner s=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x1=s.nextDouble();double y1=s.nextDouble();
System.out.print("(x2, y2):");
double x2=s.nextDouble();double y2=s.nextDouble();
System.out.print("(x3, y3):");
double x3=s.nextDouble();double y3=s.nextDouble();
double a=Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
double b=Math.sqrt(Math.pow(x3-x2,2)+Math.pow(y3-y2,2));
double c=Math.sqrt(Math.pow(x1-x3,2)+Math.pow(y1-y3,2));
double n=(a+b+c)/2;
double y=Math.sqrt(n*(n-a)*(n-b)*(n-c));
System.out.println("The area of the triangle is "+y);
}
}