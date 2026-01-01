import java.util.Scanner;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
double v0=x.hasNextDouble()?x.nextDouble():0;
double v1=x.hasNextDouble()?x.nextDouble():0;
double t=x.hasNextDouble()?x.nextDouble():0;
double n=t;
double y=0;
if(n!=0)y=(v1-v0)/n;
System.out.println("The average acceleration is "+y);
}
}