import java.util.Scanner;
public class Q1{
public static void main(String[] a){
Scanner x=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
double v0=0;
double v1=0;
double t=0;
if(x.hasNextDouble())v0=x.nextDouble();
if(x.hasNextDouble())v1=x.nextDouble();
if(x.hasNextDouble())t=x.nextDouble();
double y=v1-v0;
double n=0;
if(t!=0)n=y/t;
System.out.println("The average acceleration is "+n);
}
}