import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double y=x.nextDouble();
double n=x.nextDouble();
System.out.print("Enter x2 and y2: ");
double b=x.nextDouble();
double c=x.nextDouble();
double d=b-y;
double e=c-n;
double f=d*d;
double g=e*e;
double h=f+g;
double i=0;
if(h>=0)i=Math.sqrt(h);
System.out.println("The distance of the two points is "+i);
}
}