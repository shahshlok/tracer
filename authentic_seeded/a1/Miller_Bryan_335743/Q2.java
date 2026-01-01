import java.util.Scanner;
public class Q2 {
public static void main(String[] args) {
Scanner x=new Scanner(System.in);
double y=0;
double n=0;
double z=0;
double c=y/n*z;
System.out.print("Enter the driving distance in miles: ");
y=x.nextDouble();
System.out.print("Enter miles per gallon: ");
n=x.nextDouble();
System.out.print("Enter price in $ per gallon: ");
z=x.nextDouble();
System.out.println("The cost of driving is $"+c);
}
}