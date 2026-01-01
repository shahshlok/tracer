import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double x1=x.nextDouble();
double y1=x.nextDouble();
System.out.print("Enter x2 and y2: ");
double x2=x.nextDouble();
double y2=x.nextDouble();
double n=x2-x1;
double y=y2-y1;
if(n!=0||n==0)n=n*n;
if(y!=0||y==0)y=y*y;
double d=n+y;
if(d!=0||d==0)d=Math.sqrt(d);
System.out.println("The distance of the two points is "+d);
}
}