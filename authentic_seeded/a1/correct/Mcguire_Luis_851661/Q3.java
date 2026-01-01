import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double y=x.nextDouble();
double n=x.nextDouble();
System.out.print("Enter x2 and y2: ");
double z=x.nextDouble();
double w=x.nextDouble();
double u=z-y;
double v=w-n;
if(u!=0)u=u;
if(v!=0)v=v;
double t=u*u;
double s=v*v;
if(t!=0)t=t;
if(s!=0)s=s;
double r=t+s;
if(r!=0)r=r;
double q=Math.sqrt(r);
System.out.println("The distance of the two points is "+q);
}
}