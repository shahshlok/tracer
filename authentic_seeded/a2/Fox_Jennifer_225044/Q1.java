import java.util.*;
public class Q1{
public static void main(String[]x){
Scanner s=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int n=0,a,b,c;
for(int i=0;i<5;i++){
a=s.nextInt();
b=a%2;
c=b==0?a:0;
n+=c;
}
System.out.println("Sum of even numbers: "+n);
}
}