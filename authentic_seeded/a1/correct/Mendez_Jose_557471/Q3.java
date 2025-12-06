import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double x1=x.nextDouble(),y1=x.nextDouble();
System.out.print("Enter x2 and y2: ");
double x2=x.nextDouble(),y2=x.nextDouble();
double n=Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
System.out.println("The distance of the two points is "+n);
}
}