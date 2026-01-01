import java.util.Scanner;
public class Q4{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double a=y.nextDouble(),b=y.nextDouble();
System.out.print("(x2, y2):");
double c=y.nextDouble(),d=y.nextDouble();
System.out.print("(x3, y3):");
double e=y.nextDouble(),f=y.nextDouble();
double g=Math.sqrt(Math.pow(c-a,2)+Math.pow(d-b,2));
double h=Math.sqrt(Math.pow(e-c,2)+Math.pow(f-d,2));
double i=Math.sqrt(Math.pow(a-e,2)+Math.pow(b-f,2));
double j=(g+h+i)/2;
double k=j*(j-g)*(j-h)*(j-i);
double l=Math.sqrt(k);
System.out.println("The area of the triangle is "+l);
}
}