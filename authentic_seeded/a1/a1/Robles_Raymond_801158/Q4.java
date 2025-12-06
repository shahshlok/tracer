import java.util.*;
public class Q4{
public static void main(String[]a){
Scanner in=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x1=in.nextDouble(),y1=in.nextDouble();
System.out.print("(x2, y2):");
double x2=in.nextDouble(),y2=in.nextDouble();
System.out.print("(x3, y3):");
double x3=in.nextDouble(),y3=in.nextDouble();
double side1=Math.sqrt(x2-x1*x2-x1+y2-y1*y2-y1);
double side2=Math.sqrt(x3-x2*x3-x2+y3-y2*y3-y2);
double side3=Math.sqrt(x1-x3*x1-x3+y1-y3*y1-y3);
double s=(side1+side2+side3)/2;
double area=Math.sqrt(s*(s-side1)*(s-side2)*(s-side3));
System.out.println("The area of the triangle is "+area);
}
}
