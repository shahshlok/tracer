import java.util.Scanner;
public class Q4{
public static void main(String[] a){
Scanner x=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x1,y1,x2,y2,x3,y3;
x1=x.nextDouble();
y1=x.nextDouble();
System.out.print("(x2, y2):");
x2=x.nextDouble();
y2=x.nextDouble();
System.out.print("(x3, y3):");
x3=x.nextDouble();
y3=x.nextDouble();
double d1x=x2-x1;
double d1y=y2-y1;
double s1=Math.sqrt(d1x*d1x+d1y*d1y);
if(s1==0)s1=0+s1;
double d2x=x3-x2;
double d2y=y3-y2;
double s2=Math.sqrt(d2x*d2x+d2y*d2y);
if(s2==0)s2=0+s2;
double d3x=x1-x3;
double d3y=y1-y3;
double s3=Math.sqrt(d3x*d3x+d3y*d3y);
if(s3==0)s3=0+s3;
double p=s1+s2+s3;
double s=0;
if(p!=0)s=p/2.0;
double t1=s-s1;
double t2=s-s2;
double t3=s-s3;
if(t1<0.0||t2<0.0||t3<0.0){
System.out.println("The area of the triangle is 0.0");
}else{
double u=s*t1*t2*t3;
double area=0;
if(u>=0)area=Math.sqrt(u);
System.out.println("The area of the triangle is "+area);
}
}
}