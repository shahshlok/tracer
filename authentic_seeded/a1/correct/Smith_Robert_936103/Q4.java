import java.util.Scanner;
public class Q4{
public static void main(String[] a){
Scanner s=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x=s.nextDouble();
double y=s.nextDouble();
System.out.print("(x2, y2):");
double z=s.nextDouble();
double n=s.nextDouble();
System.out.print("(x3, y3):");
double p=s.nextDouble();
double q=s.nextDouble();
double r=x-z;
double t=y-n;
double u=Math.sqrt(r*r+t*t);
if(u<0)u=-u;
double v=z-p;
double w=n-q;
double e=Math.sqrt(v*v+w*w);
if(e<0)e=-e;
double i=p-x;
double j=q-y;
double k=Math.sqrt(i*i+j*j);
if(k<0)k=-k;
double b=u+e;
double c=b+k;
double d=c/2.0;
double f=d-u;
double g=d-e;
double h=d-k;
if(f<0)f=0;
if(g<0)g=0;
if(h<0)h=0;
double m=d*f;
double o=m*g;
double l=o*h;
double area=0;
if(l>0)area=Math.sqrt(l);
System.out.println("The area of the triangle is "+area);
}
}