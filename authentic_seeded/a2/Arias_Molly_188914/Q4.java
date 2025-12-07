import java.util.*;
public class Q4{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter height: ");
  int n=s.nextInt(),i=1,j=1,a,b,c;
  for(a=1;a<=n;a++){
   for(b=1;b<=a;b++)System.out.print("*");
   System.out.println();
  }
 }
}