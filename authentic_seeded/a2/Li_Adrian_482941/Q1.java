import java.util.*;
public class Q1{
 public static void main(String[]z){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter 5 integers: ");
  int a=0,b=0,c=0,d=0,e=0,n=0,i=0,y=0;
  for(i=1;i<5;i++){
   y=x.nextInt();
   if(i==1)a=y;
   if(i==2)b=y;
   if(i==3)c=y;
   if(i==4)d=y;
   if(i==5)e=y;
  }
  n+=(a%2==0)?a:0;
  n+=(b%2==0)?b:0;
  n+=(c%2==0)?c:0;
  n+=(d%2==0)?d:0;
  n+=(e%2==0)?e:0;
  System.out.println("Sum of even numbers: "+n);
 }
}