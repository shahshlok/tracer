import java.util.*;
public class Q4{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter height: ");
  int n=s.nextInt();
  int a=1;
  while(a<=n){
   int b=1;
   while(b<=a){
    System.out.print("*");
    b++;
   }
   System.out.println();
   a++;
  }
 }
}