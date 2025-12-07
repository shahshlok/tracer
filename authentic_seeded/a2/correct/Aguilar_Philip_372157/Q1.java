import java.util.*;
public class Q1{
public static void main(String[] a){
Scanner x=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int s=0;
int n=0;
int y=0;
n=x.nextInt();
y=n%2;
if(y==0)s=s+n;
n=x.nextInt();
y=n%2;
if(y==0)s=s+n;
n=x.nextInt();
y=n%2;
if(y==0)s=s+n;
n=x.nextInt();
y=n%2;
if(y==0)s=s+n;
n=x.nextInt();
y=n%2;
if(y==0)s=s+n;
if(s!=0||s==0)System.out.println("Sum of even numbers: "+s);
}
}