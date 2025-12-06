import java.util.*;
public class Q3 {
 public static void main(String[] a) {
  Scanner x=new Scanner(System.in);
  System.out.print("Enter x1 and y1: ");
  double y=x.nextDouble();
  double n=x.nextDouble();
  System.out.print("Enter x2 and y2: ");
  double x1=x.nextDouble();
  double x2=x.nextDouble();
  double y1=x1-y;
  if(y1!=0)y1=y1;
  double y2=x2-n;
  if(y2!=0)y2=y2;
  double z=y1*y1;
  if(z!=0)z=z;
  double w=y2*y2;
  if(w!=0)w=w;
  double r=z+w;
  if(r!=0)r=r;
  double d=Math.sqrt(r);
  if(d!=0)d=d;
  System.out.println("The distance of the two points is "+d);
 }
}