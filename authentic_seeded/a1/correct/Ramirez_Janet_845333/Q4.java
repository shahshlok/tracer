import java.util.Scanner;
public class Q4 {
    public static void main(String[] a) {
        Scanner s=new Scanner(System.in);
        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        double x=s.nextDouble();
        double y=s.nextDouble();
        System.out.print("(x2, y2):");
        double n=s.nextDouble();
        double m=s.nextDouble();
        double u=x-n;
        double v=y-m;
        double d1=Math.sqrt(u*u+v*v);
        if (d1<0) d1=-d1;
        System.out.print("(x3, y3):");
        double p=s.nextDouble();
        double q=s.nextDouble();
        u=n-p;
        v=m-q;
        double d2=Math.sqrt(u*u+v*v);
        if (d2<0) d2=-d2;
        u=x-p;
        v=y-q;
        double d3=Math.sqrt(u*u+v*v);
        if (d3<0) d3=-d3;
        double w=d1+d2+d3;
        if (w!=0) {
            double t=w/2.0;
            double r=t;
            double e=t-d1;
            double f=t-d2;
            double g=t-d3;
            if (e<0) e=e;
            if (f<0) f=f;
            if (g<0) g=g;
            double h=r*e*f*g;
            if (h<0) h=0;
            double z=Math.sqrt(h);
            if (z<0) z=-z;
            System.out.println("The area of the triangle is "+z);
        } else {
            System.out.println("The area of the triangle is 0.0");
        }
    }
}