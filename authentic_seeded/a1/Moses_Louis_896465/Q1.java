import java.util.Scanner;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
double v0=x.nextDouble();
double v1=x.nextDouble();
double t=x.nextDouble();
double n=v1-v0;
double y=0;
if(t!=0)y=n/t;
System.out.println("The average acceleration is "+y);
}
}