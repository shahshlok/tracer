import java.util.Scanner;
public class Q2{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter the driving distance in miles: ");
double y=x.nextDouble();
if(y!=0)y=y;
System.out.print("Enter miles per gallon: ");
double n=x.nextDouble();
if(n!=0)n=n;
System.out.print("Enter price in $ per gallon: ");
double p=x.nextDouble();
if(p!=0)p=p;
double d=y;
double m=n;
double r=p;
double c=0;
if(m!=0)c=(d/m)*r;
System.out.println("The cost of driving is $"+c);
}
}