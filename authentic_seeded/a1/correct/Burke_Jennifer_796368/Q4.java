import java.util.*;
public class Q4{
public static void main(String[]z){
Scanner x=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x1=x.nextDouble(),y1=x.nextDouble();
System.out.print("(x2, y2):");
double x2=x.nextDouble(),y2=x.nextDouble();
System.out.print("(x3, y3):");
double x3=x.nextDouble(),y3=x.nextDouble();
double a=x2-x1,b=y2-y1,c=Math.sqrt(a*a+b*b);
double d=x3-x2,e=y3-y2,f=Math.sqrt(d*d+e*e);
double g=x1-x3,h=y1-y3,i=Math.sqrt(g*g+h*h);
double s=(c+f+i)/2.0;
double j=s-c,k=s-f,l=s-i,m=s*j*k*l;
double n=Math.sqrt(m);
System.out.println("The area of the triangle is "+n);
}
}