import java.util.Scanner;
public class Q4{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double a=x.nextDouble(),b=x.nextDouble(),c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w;
System.out.print("(x2, y2):");
c=x.nextDouble();
d=x.nextDouble();
System.out.print("(x3, y3):");
e=x.nextDouble();
f=x.nextDouble();
g=c-a;
h=d-b;
i=g*g;
j=h*h;
k=Math.sqrt(i+j);
l=e-c;
m=f-d;
n=l*l;
o=m*m;
p=Math.sqrt(n+o);
q=a-e;
r=b-f;
s=q*q;
t=r*r;
u=Math.sqrt(s+t);
v=(k+p+u)/2.0;
w=Math.sqrt(v*(v-k)*(v-p)*(v-u));
System.out.println("The area of the triangle is "+w);
}
}