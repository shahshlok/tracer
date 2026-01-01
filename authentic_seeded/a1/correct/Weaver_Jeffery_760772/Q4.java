import java.util.Scanner;
public class Q4{
public static void main(String[]x){
Scanner n=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double a=n.nextDouble(),b=n.nextDouble(),c,d,e,f,g,h,i,j,k,l,m,s,area;
System.out.print("(x2, y2):");
c=n.nextDouble();
d=n.nextDouble();
System.out.print("(x3, y3):");
e=n.nextDouble();
f=n.nextDouble();
g=c-a;
h=d-b;
i=Math.sqrt(g*g+h*h);
j=e-c;
k=f-d;
l=Math.sqrt(j*j+k*k);
m=a-e;
s=b-f;
s=Math.sqrt(m*m+s*s);
a=(i+l+s)/2.0;
b=a-i;
c=a-l;
d=a-s;
area=Math.sqrt(a*b*c*d);
System.out.println("The area of the triangle is "+area);
}
}