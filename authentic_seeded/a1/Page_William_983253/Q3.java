import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double x1=0;double y1=0;
x.nextDouble();x.nextDouble();
System.out.print("Enter x2 and y2: ");
double x2=0;double y2=0;
x.nextDouble();x.nextDouble();
double d=Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
System.out.println("The distance of the two points is "+d);
}
}