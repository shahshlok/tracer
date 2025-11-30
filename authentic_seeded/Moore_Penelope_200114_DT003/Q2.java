
public class Q2
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the driving distance in miles: ");
        double d=scanner.nextDouble();
        System.out.print("Enter miles per gallon: ");
        double mpg=scanner.nextDouble();
        System.out.print("Enter price in $ per gallon: ");
        double p=scanner.nextDouble();
        double cost=(d/mpg)*p;
        System.out.println("The cost of driving is $" + cost);
    }
}