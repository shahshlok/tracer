import java.util.Scanner;
public class Q4{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double a=x.nextDouble();
double b=x.nextDouble();
System.out.print("(x2, y2):");
double c=x.nextDouble();
double d=x.nextDouble();
System.out.print("(x3, y3):");
double e=x.nextDouble();
double f=x.nextDouble();
double g=Math.sqrt((a-c)*(a-c)+(b-d)*(b-d));
double h=Math.sqrt((a-e)*(a-e)+(b-f)*(b-f));
double i=Math.sqrt((c-e)*(c-e)+(d-f)*(d-f));
double j=(g+h+i)/2;
double k=Math.sqrt(j*(j-g)*(j-h)*(j-i));
System.out.println("The area of the triangle is "+k);
}
}