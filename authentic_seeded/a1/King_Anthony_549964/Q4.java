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
int p=(int)i,q=(int)l,r=(int)o;
double s=(p+q+r)/2;
double t=s-p,u=s-q,v=s-r,w=s*t*u*v,z=Math.sqrt(w);
System.out.println("The area of the triangle is "+z);
}
}