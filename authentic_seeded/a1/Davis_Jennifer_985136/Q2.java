import java.util.Scanner;
public class Q2{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.print("Enter the driving distance in miles: ");
double a=x.nextDouble();
System.out.print("Enter miles per gallon: ");
double b=x.nextDouble();
System.out.print("Enter price in $ per gallon: ");
double c=x.nextDouble();
double y=a/b;
double n=y*c;
System.out.println("The cost of driving is $"+n);
}
}