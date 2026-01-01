import java.util.*;
public class Q1{
public static void main(String[]x){
Scanner s=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int a=0,b=0,c=0,n=0,y=0;
a=s.nextInt();
b=s.nextInt();
c=s.nextInt();
n=s.nextInt();
y=s.nextInt();
int z=0;
if(a%2==0)z+=a;
if(b%2==0)z+=b;
if(c%2==0)z+=c;
if(n%2==0)z+=n;
if(y%2==0)z+=y;
System.out.println("Sum of even numbers: "+z);
}
}