import java.util.*;
public class Q1{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter 5 integers: ");
  int a=0,b=0,c=0,d=0,e=0,n=0;
  a=s.nextInt();b=s.nextInt();c=s.nextInt();d=s.nextInt();e=s.nextInt();
  if(a%2==0)n+=a;
  if(b%2==0)n+=b;
  if(c%2==0)n+=c;
  if(d%2==0)n+=d;
  if(e%2==0)n+=e;
  System.out.println("Sum of even numbers: "+n);
 }
}