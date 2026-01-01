import java.util.*;
public class Q1{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter 5 integers: ");
  int a=0,b=0,c=0,n=0,y=0;
  a=x.nextInt();
  b=x.nextInt();
  c=x.nextInt();
  n=x.nextInt();
  y=x.nextInt();
  int s=0;
  if(a%2==0)s+=a;
  if(b%2==0)s+=b;
  if(c%2==0)s+=c;
  if(n%2==0)s+=n;
  if(y%2==0)s+=y;
  System.out.println("Sum of even numbers: "+s);
 }
}