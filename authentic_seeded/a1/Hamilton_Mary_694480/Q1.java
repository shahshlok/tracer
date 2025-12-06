import java.util.Scanner;
public class Q1{
public static void main(String[]x){
Scanner n=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
double v0=n.nextDouble();
double v1=n.nextDouble();
double t=n.nextDouble();
double a=v1-v0;
double b=a/t;
System.out.println("The average acceleration is "+b);
}
}