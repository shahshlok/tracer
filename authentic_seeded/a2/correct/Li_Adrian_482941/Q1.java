import java.util.*;
public class Q1{
 public static void main(String[]z){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter 5 integers: ");
  int a=0,b=0,c=0,d=0,e=0,n=0;
  a=x.nextInt();
  b=x.nextInt();
  c=x.nextInt();
  d=x.nextInt();
  e=x.nextInt();
  n+=(a%2==0)?a:0;
  n+=(b%2==0)?b:0;
  n+=(c%2==0)?c:0;
  n+=(d%2==0)?d:0;
  n+=(e%2==0)?e:0;
  System.out.println("Sum of even numbers: "+n);
 }
}