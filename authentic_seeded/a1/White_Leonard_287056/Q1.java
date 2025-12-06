import java.util.Scanner;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
double v0=x.nextDouble();
double v1=x.nextDouble();
double t=x.nextDouble();
double y=0;
double n=v1-v0;
if(n==v1-v0)y=n;
double z=0;
if(t!=0)z=y/t;
System.out.print("The average acceleration is "+z);
}
}