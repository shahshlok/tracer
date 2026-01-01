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
double o=s.nextDouble();
System.out.print("(x3, y3):");
double p=s.nextDouble();
double q=s.nextDouble();
double r=x-n;
double t=y-o;
double u=n-p;
double v=o-q;
double w=p-x;
double z=q-y;
double d=Math.sqrt(r*r+t*t);
double e=Math.sqrt(u*u+v*v);
double f=Math.sqrt(w*w+z*z);
if(d<0)d=-d;
if(e<0)e=-e;
if(f<0)f=-f;
double g=d+e;
double h=g+f;
double i=h/2.0;
double j=i-d;
double k=i-e;
double l=i-f;
double m=i*j;
if(m!=0)m=m;
double b=m*k;
if(b!=0)b=b;
double c=b*l;
if(c<0)c=-c;
double area=Math.sqrt(c);
System.out.println("The area of the triangle is "+area);
}
}