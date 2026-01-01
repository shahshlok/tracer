import java.util.Scanner;
public class Q4{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double a=y.nextDouble(),b=y.nextDouble();
System.out.print("(x2, y2):");
double c=y.nextDouble(),d=y.nextDouble();
System.out.print("(x3, y3):");
double e=y.nextDouble(),f=y.nextDouble();
double g=c-a,h=d-b,i=Math.sqrt(g*g+h*h);
double j=e-c,k=f-d,l=Math.sqrt(j*j+k*k);
double m=a-e,n=b-f,o=Math.sqrt(m*m+n*n);
double p=(i+l+o)/2,q=p-i,r=p-l,s=p-o,t=p*q*r*s,u=Math.sqrt(t);
System.out.println("The area of the triangle is "+u);
}
}