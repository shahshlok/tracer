import java.util.Scanner;
public class Q2
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the driving distance in miles: ");
        int d = scanner.nextInt();
        System.out.println("----------------");
        System.out.print("Enter miles per gallon: ");
        int mpg = scanner.nextInt();
        System.out.print("Enter price in $ per gallon: ");
        int p = scanner.nextInt();
        double cost = (d / mpg) * p;
        System.out.println("The cost of driving is $" + cost);
    }
}