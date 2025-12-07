import java.util.*;
public class Q1{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter 5 integers: ");
  int n=0;
  int a=1;
  while(a<5){
   int y=s.nextInt();
   int b=y%2;
   if(b==0)n=n+y;
   a++;
  }
  System.out.println("Sum of even numbers: "+n);
 }
}