import java.util.Scanner;
public class Q2{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter the driving distance in miles: ");
double y=0;
if(x.hasNextDouble())y=x.nextDouble();
System.out.print("Enter miles per gallon: ");
double n=0;
if(x.hasNextDouble())n=x.nextDouble();
System.out.print("Enter price in $ per gallon: ");
double z=0;
if(x.hasNextDouble())z=x.nextDouble();
double r=0;
if(n!=0)r=(y/n)*z;
System.out.print("The cost of driving is $");
System.out.print(r);
}
}