import java.util.Scanner;
public class Q3 {
    public static void main(String[] args) {
        Scanner x=new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double a=x.nextDouble(),b=x.nextDouble();
        System.out.print("Enter x2 and y2: ");
        double c=x.nextDouble(),d=x.nextDouble();
        double e=c-a,f=d-b,g=Math.sqrt(e*e+f*f);
        System.out.println("The distance of the two points is "+g);
    }
}