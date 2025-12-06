import java.util.*;
public class Main{
public static void main(String[]args){
Scanner s=new Scanner(System.in);
System.out.print("Enter the driving distance in miles: ");
double d=s.nextDouble();
System.out.print("Enter miles per gallon: ");
double mpg=s.nextDouble();
System.out.print("Enter price in $ per gallon: ");
double p=s.nextDouble();
double cost=(d/mpg)*p;
System.out.println("The cost of driving is $"+cost);
}
}
