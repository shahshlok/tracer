import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double x=s.nextDouble();
double y=s.nextDouble();
System.out.print("Enter x2 and y2: ");
double n=s.nextDouble();
double z=s.nextDouble();
double d=Math.sqrt((n-x)*(n-x)+(z-y)*(z-y));
System.out.println("The distance of the two points is "+d);
}
}