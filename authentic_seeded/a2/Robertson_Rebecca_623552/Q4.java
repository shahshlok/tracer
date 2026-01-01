import java.util.*;
public class Q4{
 public static void main(String[]a){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter height: ");
  int n=0;
  if(x!=null)n=x.nextInt();
  int i=1;
  int j=0;
  if(n>0){
   while(i<=n){
    j=0;
    if(i>0){
     while(j<i){
      System.out.print("*");
      i=i+1;
      if(i>n)i=n;
     }
    }
    System.out.println();
    i=i+1;
   }
  }
 }
}