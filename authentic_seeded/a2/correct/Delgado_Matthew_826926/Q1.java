import java.util.*;
public class Q1{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter 5 integers: ");
  int n=5;
  int a=0;
  while(n>0){
   int y=s.nextInt();
   int b=y%2;
   if(b==0)a=a+y;
   n--;
  }
  System.out.println("Sum of even numbers: "+a);
 }
}