import java.util.Scanner;
public class Q2{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.print("Enter the driving distance in miles: ");
double n=y.nextDouble();
System.out.print("Enter miles per gallon: ");
double a=y.nextDouble();
System.out.print("Enter price in $ per gallon: ");
double b=y.nextDouble();
System.out.print("The cost of driving is $"+(n/a*b));
}
}