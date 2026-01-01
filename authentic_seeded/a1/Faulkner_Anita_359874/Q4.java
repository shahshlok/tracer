import java.util.Scanner;
public class Q4{
public static void main(String[] x){
Scanner n=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double a=n.nextDouble(),b=n.nextDouble();
System.out.print("(x2, y2):");
double c=n.nextDouble(),d=n.nextDouble();
System.out.print("(x3, y3):");
double e=n.nextDouble(),f=n.nextDouble();
double g=Math.sqrt((a-c)*(a-c)+(b-d)*(b-d));
double h=Math.sqrt((a-e)*(a-e)+(b-f)*(b-f));
double i=Math.sqrt((c-e)*(c-e)+(d-f)*(d-f));
int j=(int)(g+h+i);
double k=(double)(j/2);
double l=Math.sqrt(k*(k-g)*(k-h)*(k-i));
System.out.println("The area of the triangle is "+l);
}
}