import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double y=x.hasNextDouble()?x.nextDouble():0;
double n=x.hasNextDouble()?x.nextDouble():0;
System.out.print("Enter x2 and y2: ");
double z=x.hasNextDouble()?x.nextDouble():0;
double w=x.hasNextDouble()?x.nextDouble():0;
double u=z-y;
double v=w-n;
double t=u*u;
double s=v*v;
double r=t+s;
double q=Math.sqrt(r);
if(q!=0||q==0)System.out.println("The distance of the two points is "+q);
x.close();
}
}