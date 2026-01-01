import java.util.*;
public class Q1{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int n=0;
for(int i=0;i<5;i++){int a=y.nextInt();int s=0;if(a%2==0){s+=a;n=s;}}
System.out.println("Sum of even numbers: "+n);
}
}