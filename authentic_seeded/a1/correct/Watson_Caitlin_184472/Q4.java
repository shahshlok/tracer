import java.util.Scanner;
public class Q4{
public static void main(String[]args){
Scanner s=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x=s.nextDouble(),y=s.nextDouble(),a,b,c,d,e,f,g,h,i,j,k,l,m,n;
System.out.print("(x2, y2):");
a=s.nextDouble();b=s.nextDouble();
System.out.print("(x3, y3):");
c=s.nextDouble();d=s.nextDouble();
e=a-x;f=b-y;g=Math.sqrt(e*e+f*f);
h=c-a;i=d-b;j=Math.sqrt(h*h+i*i);
k=x-c;l=y-d;m=Math.sqrt(k*k+l*l);
n=(g+j+m)/2.0;
double o=n*(n-g)*(n-j)*(n-m);
double p=Math.sqrt(o);
System.out.println("The area of the triangle is "+p);
}
}