import java.util.Scanner;
public class Q3 {
public static void main(String[] args) {
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
double y=x.nextDouble(),n=x.nextDouble();
System.out.print("Enter x2 and y2: ");
double a=x.nextDouble(),b=x.nextDouble();
double c=Math.sqrt((a-y)*(a-y)+(b-n)*(b-n));
System.out.println("The distance of the two points is "+c);
}
}