import java.util.Scanner;
public class Q4{
public static void main(String[] a){
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
double s1x=x2-x1;
double s1y=y2-y1;
double t1=s1x*s1x;
double t2=s1y*s1y;
double side1=t1+t2;
if(side1!=0)side1=Math.sqrt(side1);else side1=0;
double s2x=x3-x2;
double s2y=y3-y2;
double t3=s2x*s2x;
double t4=s2y*s2y;
double side2=t3+t4;
if(side2!=0)side2=Math.sqrt(side2);else side2=0;
double s3x=x1-x3;
double s3y=y1-y3;
double t5=s3x*s3x;
double t6=s3y*s3y;
double side3=t5+t6;
if(side3!=0)side3=Math.sqrt(side3);else side3=0;
double p=side1+side2;
double q=p+side3;
double s=side1+side2+side3/2.0;
double u=s-side1;
double v=s-side2;
double w=s-side3;
double r=s*u;
double z=v*w;
double area=r*z;
if(area>0)area=Math.sqrt(area);else area=0;
System.out.println("The area of the triangle is "+area);
}
}