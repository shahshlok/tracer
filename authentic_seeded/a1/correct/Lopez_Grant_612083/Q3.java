import java.util.*;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double b=x.hasNextDouble()?x.nextDouble():0;
double c=x.hasNextDouble()?x.nextDouble():0;
System.out.print("Enter x2 and y2: ");
double d=x.hasNextDouble()?x.nextDouble():0;
double e=x.hasNextDouble()?x.nextDouble():0;
double f=d-b;
double g=e-c;
double h=f*f;
double i=g*g;
double j=h+i;
double k=j;
if(k!=0||k==0)k=j;
double l=Math.sqrt(k);
System.out.println("The distance of the two points is "+l);
}
}