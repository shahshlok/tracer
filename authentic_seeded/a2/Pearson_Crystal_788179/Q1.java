import java.util.*;
public class Q1{
public static void main(String[]x){
Scanner s=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int n=0,y=0,a=0,b=0,c=0;
for(int i=0;i<5;i++){
n=s.nextInt();
a=n%2;
b=a==0?1:0;
c=n*b;
y+=c;
}
System.out.println("Sum of even numbers: "+y);
}
}