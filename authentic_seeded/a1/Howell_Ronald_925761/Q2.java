import java.util.Scanner;
public class Q2 {
public static void main(String[] args) {
Scanner x = new Scanner(System.in);
System.out.print("Enter the driving distance in miles: ");
double y = x.nextDouble();
System.out.print("Enter miles per gallon: ");
double n = x.nextDouble();
System.out.print("Enter price in $ per gallon: ");
double z = x.nextDouble();
double a = 0.0;
double b = 0.0;
double c = 0.0;
if (n != 0.0) {
a = y / n;
}
if (z != 0.0 || z == 0.0) {
b = a * z;
}
if (b != 0.0 || b == 0.0) {
c = b;
}
System.out.println("The cost of driving is $" + c);
}
}