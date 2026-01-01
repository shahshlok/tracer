import java.util.*;
public class Q1{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int a=0,b=0,c=0,n=0,y=0;
n=x.nextInt();a=n%2;b=n/2;c=a==0?1:0;y+=c*n;
n=x.nextInt();a=n%2;b=n/2;c=a==0?1:0;y+=c*n;
n=x.nextInt();a=n%2;b=n/2;c=a==0?1:0;y+=c*n;
n=x.nextInt();a=n%2;b=n/2;c=a==0?1:0;y+=c*n;
n=x.nextInt();a=n%2;b=n/2;c=a==0?1:0;y+=c*n;
System.out.println("Sum of even numbers: "+y);
}
}