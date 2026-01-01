import java.util.Scanner;
public class Q2{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter the driving distance in miles: ");
double y=x.nextDouble();
System.out.print("Enter miles per gallon: ");
double n=x.nextDouble();
System.out.print("Enter price in $ per gallon: ");
double z=x.nextDouble();
double w=y/n*z;
System.out.println("The cost of driving is $"+w);
}
}