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
double n7=n1*n1;
double n8=n2*n2;
double n9=n3*n3;
double n10=n4*n4;
double n11=n5*n5;
double n12=n6*n6;
double n13=n7+n8;
double n14=n9+n10;
double n15=n11+n12;
int side1=0;
int side2=0;
int side3=0;
if(n13>=0){
side1=(int)Math.sqrt(n13);
}
if(n14>=0){
side2=(int)Math.sqrt(n14);
}
if(n15>=0){
side3=(int)Math.sqrt(n15);
}
int n16=side1+side2;
int n17=n16+side3;
double n18=2;
double n19=n17/2;
double n20=n19;
double n21=n20-side1;
double n22=n20-side2;
double n23=n20-side3;
double n24=n20*n21;
double n25=n22*n23;
double n26=n24*n25;
double area=0;
if(n26>=0){
area=Math.sqrt(n26);
}
System.out.println("The area of the triangle is "+area);
}
}