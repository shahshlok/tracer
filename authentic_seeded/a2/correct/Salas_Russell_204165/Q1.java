import java.util.*;
public class Q1{
public static void main(String[]x){
Scanner s=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int a=0,b=0,c=0,d=0,e=0;
a=s.nextInt();
b=s.nextInt();
c=s.nextInt();
d=s.nextInt();
e=s.nextInt();
int y=0,n=0;
y=(a%2==0)?a:0;
n+=(y);
y=(b%2==0)?b:0;
n+=(y);
y=(c%2==0)?c:0;
n+=(y);
y=(d%2==0)?d:0;
n+=(y);
y=(e%2==0)?e:0;
n+=(y);
System.out.println("Sum of even numbers: "+n);
}
}