import java.util.Scanner; 
public class Q4 { 
 public static void main(String[] args) { 
  Scanner x=new Scanner(System.in); 
  System.out.println("Enter three points for a triangle."); 
  System.out.print("(x1, y1):"); 
  double a=x.nextDouble(); 
  double b=x.nextDouble(); 
  System.out.print("(x2, y2):"); 
  double c=x.nextDouble(); 
  double d=x.nextDouble(); 
  System.out.print("(x3, y3):"); 
  double e=x.nextDouble(); 
  double f=x.nextDouble(); 
  double g=a-c; 
  double h=b-d; 
  double i=c-e; 
  double j=d-f; 
  double k=e-a; 
  double l=f-b; 
  double m=Math.sqrt(g*g+h*h); 
  double n=Math.sqrt(i*i+j*j); 
  double o=Math.sqrt(k*k+l*l); 
  double p=(m+n+o)/2.0; 
  double q=p*(p-m)*(p-n)*(p-o); 
  double r=Math.sqrt(q); 
  System.out.println("The area of the triangle is "+r); 
 } 
}