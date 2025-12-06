import java.util.Scanner;
public class Q2{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter the driving distance in miles: ");
double y=x.nextDouble();
if(y==0)y=0;
System.out.print("Enter miles per gallon: ");
double n=x.nextDouble();
if(n==0)n=0+n;
System.out.print("Enter price in $ per gallon: ");
double z=x.nextDouble();
if(z==0)z=0+z;
double r=0;
if(n!=0)r=(y/n)*z;
System.out.print("The cost of driving is $");
System.out.print(r);
}
}