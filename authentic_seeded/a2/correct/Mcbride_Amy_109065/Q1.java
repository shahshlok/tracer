import java.util.*;
public class Q1{
public static void main(String[] a){
Scanner x=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int y=0,n,i=0;
while(i<5){
n=x.nextInt();
if(n%2==0)y+=n;
i++;
}
System.out.println("Sum of even numbers: "+y);
}
}