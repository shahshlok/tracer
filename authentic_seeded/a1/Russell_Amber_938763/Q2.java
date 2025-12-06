import java.util.Scanner;
public class Q2{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
double y=0;
double n=0;
double z=0;
double a=y;
double b=n;
double c=z;
double d=0;
if(b!=0)d=(a/b)*c;
System.out.print("Enter the driving distance in miles: ");
y=x.nextDouble();
a=y;
System.out.print("Enter miles per gallon: ");
n=x.nextDouble();
b=n;
System.out.print("Enter price in $ per gallon: ");
z=x.nextDouble();
c=z;
System.out.println("The cost of driving is $"+d);
}
}