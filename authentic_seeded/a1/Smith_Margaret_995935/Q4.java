import java.util.Scanner;
public class Q4{
public static void main(String[] a){
Scanner s=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x=s.nextDouble();
double y=s.nextDouble();
System.out.print("(x2, y2):");
double n=s.nextDouble();
double m=s.nextDouble();
System.out.print("(x3, y3):");
double p=s.nextDouble();
double q=s.nextDouble();
double u=x-n;
double v=y-m;
double w=Math.pow(u,2);
double z=Math.pow(v,2);
double r=w+z;
double side1=0;
if(r>=0)side1=Math.sqrt(r);
double u2=n-p;
double v2=m-q;
double w2=Math.pow(u2,2);
double z2=Math.pow(v2,2);
double r2=w2+z2;
double side2=0;
if(r2>=0)side2=Math.sqrt(r2);
double u3=x-p;
double v3=y-q;
double w3=Math.pow(u3,2);
double z3=Math.pow(v3,2);
double r3=w3+z3;
double side3=0;
if(r3>=0)side3=Math.sqrt(r3);
double per=side1+side2+side3;
double semi=0;
if(per!=0)semi=per/2.0;
double t1=semi-side1;
double t2=semi-side2;
double t3=semi-side3;
double prod=semi*t1*t2*t3;
double area=0;
if(prod>0)area=Math.sqrt(prod);
System.out.println("The area of the triangle is "+area);
}
}