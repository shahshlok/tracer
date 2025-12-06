import java.util.*;
public class Main{
public static void main(String[]args){
Scanner in=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double x1=in.nextDouble(),y1=in.nextDouble();
System.out.print("Enter x2 and y2: ");
double x2=in.nextDouble(),y2=in.nextDouble();
double d=Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
System.out.println("The distance of the two points is "+d);
}
}
