import java.util.Scanner;
public class Q3 {
    public static void main(String[] a) {
        Scanner x=new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        int x1=x.nextInt();
        int y1=x.nextInt();
        System.out.print("Enter x2 and y2: ");
        int x2=x.nextInt();
        int y2=x.nextInt();
        int n=(x2-x1)^(x2-x1)+(y2-y1)^(y2-y1);
        System.out.println("The distance of the two points is "+n);
    }
}