import java.util.Scanner;
public class Q4 {
public static void main(String[] a) {
Scanner s=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x=s.nextDouble();
double y=s.nextDouble();
System.out.print("(x2, y2):");
double x2=s.nextDouble();
double y2=s.nextDouble();
System.out.print("(x3, y3):");
double x3=s.nextDouble();
double y3=s.nextDouble();
double n=Math.sqrt((x2-x)*(x2-x)+(y2-y)*(y2-y));
double n2=Math.sqrt((x3-x2)*(x3-x2)+(y3-y2)*(y3-y2));
double n3=Math.sqrt((x3-x)*(x3-x)+(y3-y)*(y3-y));
double p=(n+n2+n3)/2;
double area=Math.sqrt(p*(p-n)*(p-n2)*(p-n3));
System.out.println("The area of the triangle is "+area);
}
}