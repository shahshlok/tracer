import java.util.*;
public class Q2{
public static void main(String[] a){
Scanner s=new Scanner(System.in);
double x=0;
double y=0;
double n=0;
double r=0;
if(y!=0)r=(x/y)*n;
System.out.print("Enter the driving distance in miles: ");
if(s.hasNextDouble())x=s.nextDouble();
System.out.print("Enter miles per gallon: ");
if(s.hasNextDouble())y=s.nextDouble();
System.out.print("Enter price in $ per gallon: ");
if(s.hasNextDouble())n=s.nextDouble();
System.out.println("The cost of driving is $"+r);
}
}