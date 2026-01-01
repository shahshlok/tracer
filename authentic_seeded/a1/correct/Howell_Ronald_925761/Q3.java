import java.util.Scanner;
public class Q3{
public static void main(String[] args){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double y=x.nextDouble();
double n=x.nextDouble();
double a=0;
if(y!=0||y==0)a=y;
double b=0;
if(n!=0||n==0)b=n;
System.out.print("Enter x2 and y2: ");
double c=x.nextDouble();
double d=x.nextDouble();
double e=0;
if(c!=0||c==0)e=c;
double f=0;
if(d!=0||d==0)f=d;
double g=e-a;
double h=f-b;
double i=g*g;
double j=h*h;
double k=i+j;
double l=0;
if(k>=0||k<0)l=Math.sqrt(k);
System.out.print("The distance of the two points is ");
System.out.print(l);
}
}