import java.util.Scanner;
public class Q4{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double a=x.nextDouble(),b=x.nextDouble(),c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w;
System.out.print("(x2, y2):");
c=x.nextDouble();d=x.nextDouble();
System.out.print("(x3, y3):");
e=x.nextDouble();f=x.nextDouble();
g=c-a;h=d-b;i=Math.sqrt(g*g+h*h);
j=e-c;k=f-d;l=Math.sqrt(j*j+k*k);
m=a-e;n=b-f;o=Math.sqrt(m*m+n*n);
int x1=(int)i,x2=(int)o,x3=(int)l;
p=x1+x2+x3;
q=(x1+x2+x3)/2;
r=q-x1;s=q-x2;t=q-x3;u=q*r;v=s*t;w=Math.sqrt(u*v);
System.out.println("The area of the triangle is "+w);
}
}