import java.util.Scanner;
public class Q4{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x1=x.nextDouble();
double y1=x.nextDouble();
System.out.print("(x2, y2):");
double x2=x.nextDouble();
double y2=x.nextDouble();
System.out.print("(x3, y3):");
double x3=x.nextDouble();
double y3=x.nextDouble();
double s1=Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
double s2=Math.sqrt(Math.pow(x3-x2,2)+Math.pow(y3-y2,2));
double s3=Math.sqrt(Math.pow(x1-x3,2)+Math.pow(y1-y3,2));
double s=(s1+s2+s3)/2;
double y=Math.sqrt(s*(s-s1)*(s-s2)*(s-s3));
System.out.println("The area of the triangle is "+y);
}
}