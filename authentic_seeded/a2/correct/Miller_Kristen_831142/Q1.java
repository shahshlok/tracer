import java.util.*;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int s=0;
for(int i=0;i<5;i++){
int n=x.nextInt();
if(n%2==0)s+=n;
}
System.out.println("Sum of even numbers: "+s);
}
}