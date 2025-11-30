package mycode;
import java.util.Scanner;
public class Q2 {
// TODO: Clean up code before submission
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter the driving distance in miles: ");
    double distance = sc.nextDouble();
    System.out.print("Enter miles per gallon: ");
    double milesPerGallon = sc.nextDouble();
    System.out.print("Enter price in $ per gallon: ");
    double price = sc.nextDouble();
    double cost = (distance / milesPerGallon) * price;
    System.out.println("The cost of driving is $" + cost);
  }
}