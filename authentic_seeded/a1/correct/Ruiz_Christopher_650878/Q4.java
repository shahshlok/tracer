import java.util.Scanner;
public class Q4{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.println("Enter three points for a triangle.");
System.out.print("(x1, y1):");
double x=s.nextDouble();
double y=s.nextDouble();
double x2=0;
double y2=0;
double x3=0;
double y3=0;
if(x==x){
x2=s.nextDouble();
}
if(y==y){
y2=s.nextDouble();
}
System.out.print("(x2, y2):");
if(x2==x2){
x2=x2;
}
if(y2==y2){
y2=y2;
}
if(x2==x2){
x3=s.nextDouble();
}
if(y2==y2){
y3=s.nextDouble();
}
System.out.print("(x3, y3):");
if(x3==x3){
x3=x3;
}
if(y3==y3){
y3=y3;
}
double n1=x2-x;
double n2=y2-y;
double n3=x3-x2;
double n4=y3-y2;
double n5=x3-x;
double n6=y3-y;
double side1=Math.sqrt(n1*n1+n2*n2);
double side2=Math.sqrt(n3*n3+n4*n4);
double side3=Math.sqrt(n5*n5+n6*n6);
double n7=side1+side2+side3;
double n8=0.5;
double n9=n7*n8;
double n=n9;
double n10=n-side1;
double n11=n-side2;
double n12=n-side3;
double n13=n*n10;
double n14=n11*n12;
double n15=n13*n14;
double area=0;
if(n15>=0){
area=Math.sqrt(n15);
}
System.out.println("The area of the triangle is "+area);
}
}