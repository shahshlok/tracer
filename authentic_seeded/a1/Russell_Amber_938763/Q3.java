import java.util.*;
public class Q3{
public static void main(String[] a){
Scanner s=new Scanner(System.in);
double x=0;
double y=0;
double n=0;
System.out.print("Enter x1 and y1: ");
if(s.hasNextDouble())x=s.nextDouble();
if(s.hasNextDouble())y=s.nextDouble();
double x2=0;
double y2=0;
System.out.print("Enter x2 and y2: ");
if(s.hasNextDouble())x2=s.nextDouble();
if(s.hasNextDouble())y2=s.nextDouble();
double dx=x2-x;
double dy=y2-y;
double dx2=dx*dx;
double dy2=dy*dy;
n=dx2+dy2;
double d=0;
if(n>=0)d=Math.sqrt(n);
System.out.println("The distance of the two points is "+d);
}
}