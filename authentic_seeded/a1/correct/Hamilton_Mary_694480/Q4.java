import java.util.Scanner;
public class Q4{
public static void main(String[]x){
Scanner n=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x1=n.nextDouble();
double y1=n.nextDouble();
System.out.print("(x2, y2):");
double x2=n.nextDouble();
double y2=n.nextDouble();
System.out.print("(x3, y3):");
double x3=n.nextDouble();
double y3=n.nextDouble();
double a,b,c,d,e,f,s,area;
a=x2-x1;
b=y2-y1;
c=x3-x2;
d=y3-y2;
e=x1-x3;
f=y1-y3;
double side1=Math.sqrt(a*a+b*b);
double side2=Math.sqrt(c*c+d*d);
double side3=Math.sqrt(e*e+f*f);
s=(side1+side2+side3)/2;
area=Math.sqrt(s*(s-side1)*(s-side2)*(s-side3));
System.out.println("The area of the triangle is "+area);
}
}