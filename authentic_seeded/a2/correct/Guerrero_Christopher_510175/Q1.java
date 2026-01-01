import java.util.*;
public class Q1{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int n=0,a=0,b=0,c=0;
a=y.nextInt();
b=y.nextInt();
c=y.nextInt();
n+=(a%2==0?a:0);
n+=(b%2==0?b:0);
n+=(c%2==0?c:0);
a=y.nextInt();
b=y.nextInt();
n+=(a%2==0?a:0);
n+=(b%2==0?b:0);
System.out.println("Sum of even numbers: "+n);
}
}