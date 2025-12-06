import java.util.Scanner;
public class Q1{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
double v0=y.nextDouble();
double v1=y.nextDouble();
double t=y.nextDouble();
double a,b,c;
a=v1-v0;
b=t;
c=a/b;
System.out.println("The average acceleration is "+c);
}
}