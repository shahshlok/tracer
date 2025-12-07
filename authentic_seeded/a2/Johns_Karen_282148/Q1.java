import java.util.*;
public class Q1{
 public static void main(String[]x){
  Scanner n=new Scanner(System.in);
  System.out.print("Enter 5 integers: ");
  int a=0,b=0,c=0,y=0;
  for(int i=1;i<5;i++){
   a=n.nextInt();
   b=a%2;
   c=a*(1-b*b);
   y=y+c;
  }
  System.out.println("Sum of even numbers: "+y);
 }
}