import java.util.Scanner;
public class Q2{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter the driving distance in miles: ");
double y=0;
if(x.hasNextDouble())y=x.nextDouble();
double n=y;
System.out.print("Enter miles per gallon: ");
double m=0;
if(x.hasNextDouble())m=x.nextDouble();
double g=m;
System.out.print("Enter price in $ per gallon: ");
double p=0;
if(x.hasNextDouble())p=x.nextDouble();
double q=p;
double r=0;
if(g!=0)r=n/g;
double s=0;
if(q!=0||q==0)s=r*q;
System.out.println("The cost of driving is $"+s);
}
}