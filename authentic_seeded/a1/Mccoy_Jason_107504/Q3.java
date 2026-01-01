import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double y=0;
double n=0;
double b=0;
double c=0;
double z=x.nextDouble();
double w=x.nextDouble();
if(z!=0||w!=0){
}
System.out.print("Enter x2 and y2: ");
double u=x.nextDouble();
double v=x.nextDouble();
if(u!=0||v!=0){
}
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