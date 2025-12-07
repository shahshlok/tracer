import java.util.*;
public class Q4{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter height: ");
  int n=s.nextInt();
  int i=1;
  for(i=1;i<=n;i++){
   int j=1;
   for(j=1;j<=i;j++)System.out.print("*");
   System.out.println();
  }
 }
}