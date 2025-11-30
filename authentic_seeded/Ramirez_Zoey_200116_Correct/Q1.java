// Name: Zoey Ramirez
// Student ID: 200116
// Assignment 2



public class Q1 {
    public static void main(String[] args)
    {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double vStart = reader.nextDouble();
        double vEnd = reader.nextDouble();
        double t = reader.nextDouble();
        double a = (vEnd - vStart) / t;
        System.out.println("The average acceleration is " + a);
    }
}