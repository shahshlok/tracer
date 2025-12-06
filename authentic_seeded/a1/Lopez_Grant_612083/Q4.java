import java.util.Scanner;
public class Q4{
public static void main(String[] a){
Scanner s=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x=s.nextDouble();
double y=s.nextDouble();
double x1=x;
double y1=y;
System.out.print("(x2, y2):");
x=s.nextDouble();
y=s.nextDouble();
double x2=x;
double y2=y;
System.out.print("(x3, y3):");
x=s.nextDouble();
y=s.nextDouble();
double x3=x;
double y3=y;
double n=x2-x1;
double m=y2-y1;
double d1=Math.sqrt(n*n+m*m);
n=x3-x2;
m=y3-y2;
double d2=Math.sqrt(n*n+m*m);
n=x1-x3;
m=y1-y3;
double d3=Math.sqrt(n*n+m*m);
double p=d1+d2+d3;
double q=0;
if(p!=0)q=p/2.0;
double r=q-d1;
double t=q-d2;
double u=q-d3;
double v=q;
if(v<0)v=0;
if(r<0)r=0;
if(t<0)t=0;
if(u<0)u=0;
double w=v*r*t*u;
double area=0;
if(w>0)area=Math.sqrt(w);
System.out.println("The area of the triangle is "+area);
}
}