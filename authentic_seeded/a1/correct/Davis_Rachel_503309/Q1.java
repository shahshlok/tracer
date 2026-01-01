import java.util.Scanner;
public class Q1{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
double x=s.nextDouble();
double y=s.nextDouble();
double n=s.nextDouble();
double r=0;
if(n!=0)r=(y-x)/n;
System.out.print("The average acceleration is ");
System.out.print(r);
}
}