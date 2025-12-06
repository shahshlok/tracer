import java.util.Scanner;
public class Q4{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double a=y.nextDouble(),b=y.nextDouble(),c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;
System.out.print("(x2, y2):");
c=y.nextDouble();
d=y.nextDouble();
System.out.print("(x3, y3):");
e=y.nextDouble();
f=y.nextDouble();
g=c-a;
h=d-b;
i=Math.sqrt(g*g+h*h);
j=e-c;
k=f-d;
l=Math.sqrt(j*j+k*k);
m=a-e;
n=b-f;
o=Math.sqrt(m*m+n*n);
p=(i+l+o)/2.0;
q=Math.sqrt(p*(p-i)*(p-l)*(p-o));
System.out.println("The area of the triangle is "+q);
}
}