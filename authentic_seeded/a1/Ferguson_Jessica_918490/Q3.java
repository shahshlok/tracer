import java.util.*;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double x1=x.nextDouble();
double y1=x.nextDouble();
double t1=x1;
double t2=y1;
System.out.print("Enter x2 and y2: ");
double x2=x.nextDouble();
double y2=x.nextDouble();
double t3=x2;
double t4=y2;
double n=t3-t1;
double m=t4-t2;
double d=0;
if(n!=0||m!=0)d=Math.sqrt(n*n+m*m);
System.out.println("The distance of the two points is "+d);
}
}