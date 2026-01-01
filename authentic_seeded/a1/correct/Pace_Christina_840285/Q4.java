import java.util.*;
public class Q4{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x=s.nextDouble(),y=s.nextDouble(),n;
System.out.print("(x2, y2):");
double x2=s.nextDouble(),y2=s.nextDouble();
System.out.print("(x3, y3):");
double x3=s.nextDouble(),y3=s.nextDouble();
double side1=Math.sqrt((x2-x)*(x2-x)+(y2-y)*(y2-y));
double side2=Math.sqrt((x3-x2)*(x3-x2)+(y3-y2)*(y3-y2));
double side3=Math.sqrt((x3-x)*(x3-x)+(y3-y)*(y3-y));
n=(side1+side2+side3)/2.0;
double area=Math.sqrt(n*(n-side1)*(n-side2)*(n-side3));
System.out.println("The area of the triangle is "+area);
}
}