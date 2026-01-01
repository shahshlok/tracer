import java.util.*;
public class Q1{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter 5 integers: ");
  int a=0,b=0,c=0,n=0,y=0;
  int s=0;
  for(int i=1;i<5;i++){
   int t=x.nextInt();
   if(i==1)a=t;
   if(i==2)b=t;
   if(i==3)c=t;
   if(i==4)n=t;
   if(t%2==0)s+=t;
  }
  System.out.println("Sum of even numbers: "+s);
 }
}