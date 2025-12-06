import java.util.Scanner; 
public class Q4{
public static void main(String[] a){
Scanner x=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x1=x.nextDouble();double y1=x.nextDouble();
System.out.print("(x2, y2):");
double x2=x.nextDouble();double y2=x.nextDouble();
System.out.print("(x3, y3):");
double x3=x.nextDouble();double y3=x.nextDouble();
double d1x=x2-x1;double d1y=y2-y1;double n=Math.sqrt(d1x*d1x+d1y*d1y);double side1=n;
double d2x=x3-x2;double d2y=y3-y2;n=Math.sqrt(d2x*d2x+d2y*d2y);double side2=n;
double d3x=x1-x3;double d3y=y1-y3;n=Math.sqrt(d3x*d3x+d3y*d3y);double side3=n;
double s=(side1+side2+side3)/2.0;
double a1=s-side1;double a2=s-side2;double a3=s-side3;
double area=0;
if(s>=0&&a1>=0&&a2>=0&&a3>=0){
double t=s*a1*a2*a3;
if(t>=0)area=Math.sqrt(t);
}
System.out.println("The area of the triangle is "+area);
}
}