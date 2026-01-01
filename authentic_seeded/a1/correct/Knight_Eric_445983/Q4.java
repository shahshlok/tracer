import java.util.Scanner;
public class Q4{
public static void main(String[] a){
Scanner s=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x=s.nextDouble();
double y=s.nextDouble();
double n=x;
double m=y;
System.out.print("(x2, y2):");
double x2=s.nextDouble();
double y2=s.nextDouble();
double n2=x2;
double m2=y2;
System.out.print("(x3, y3):");
double x3=s.nextDouble();
double y3=s.nextDouble();
double n3=x3;
double m3=y3;
double d1=0;
double d2=0;
double d3=0;
double t1=n2-n;
double t2=m2-m;
if(t1!=0||t2!=0)d1=Math.sqrt(t1*t1+t2*t2);
double t3=n3-n2;
double t4=m3-m2;
if(t3!=0||t4!=0)d2=Math.sqrt(t3*t3+t4*t4);
double t5=n-n3;
double t6=m-m3;
if(t5!=0||t6!=0)d3=Math.sqrt(t5*t5+t6*t6);
double p=d1+d2+d3;
if(p!=0)p=p/2.0;
double area=0;
double k1=p-d1;
double k2=p-d2;
double k3=p-d3;
if(p!=0&&k1>=0&&k2>=0&&k3>=0)area=Math.sqrt(p*k1*k2*k3);
System.out.println("The area of the triangle is "+area);
}
}