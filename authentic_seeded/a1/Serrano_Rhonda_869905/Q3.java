import java.util.*;
public class Q3{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
int a=x.nextInt();
int b=x.nextInt();
System.out.print("Enter x2 and y2: ");
int c=x.nextInt();
int d=x.nextInt();
int e=c-a;
int f=d-b;
int g=e^2;
int h=f^2;
int i=g+h;
double j=Math.sqrt(i);
System.out.println("The distance of the two points is "+j);
}
}