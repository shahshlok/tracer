import java.util.Scanner;
public class Q3 {
    public static void main(String[] args) {
        Scanner x=new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double y=x.nextDouble();
        double n=x.nextDouble();
        System.out.print("Enter x2 and y2: ");
        double a=x.nextDouble();
        double b=x.nextDouble();
        double c=a-y;
        double d=b-n;
        double e=c*c+d*d;
        double f=Math.sqrt(e);
        System.out.println("The distance of the two points is "+f);
    }
}