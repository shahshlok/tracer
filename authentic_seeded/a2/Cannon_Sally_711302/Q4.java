import java.util.*;
public class Q4{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter height: ");
  int n=x.nextInt();
  for(int a=1;a<n;a++){
   for(int b=1;b<=a;b++)System.out.print("*");
   System.out.println();
  }
 }
}