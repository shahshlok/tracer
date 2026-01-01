import java.util.*;
public class Q4{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter height: ");
  int n=x.nextInt();
  int a=1,b=1,c=1;
  for(a=1;a<=n;a++){
   for(b=1;b<=a;b++){
    System.out.print("*");
    a++;
   }
   System.out.println();
  }
 }
}