import java.util.*;
public class Q4{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double a=x.nextDouble(),b=x.nextDouble();
System.out.print("(x2, y2):");
double c=x.nextDouble(),d=x.nextDouble();
System.out.print("(x3, y3):");
double e=x.nextDouble(),f=x.nextDouble();
double g=c-a,h=d-b,i=e-c,j=f-d,k=a-e,l=b-f;
double m=Math.sqrt(g*g+h*h),n=Math.sqrt(i*i+j*j),o=Math.sqrt(k*k+l*l);
double p=(m+n+o)/2,q=p*(p-m)*(p-n)*(p-o),r=Math.sqrt(q);
System.out.println("The area of the triangle is "+r);
}
}