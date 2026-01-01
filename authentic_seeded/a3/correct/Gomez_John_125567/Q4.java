import java.util.*;
public class Q4{
 public static void main(String[]a){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter size: ");
  int n=x.nextInt();
  if(n<0)n=0;
  int[] y=new int[n];
  System.out.print("Enter elements: ");
  int i=0;
  while(i<n){
   int t=x.nextInt();
   y[i]=t;
   i++;
  }
  if(n>0){
   int t=y[n-1];
   int j=n-1;
   while(j>0){
    int h=y[j-1];
    y[j]=h;
    j--;
   }
   y[0]=t;
  }
  System.out.print("Shifted: ");
  int k=0;
  while(k<n){
   System.out.print(y[k]);
   if(k+1<n)System.out.print(" ");
   k++;
  }
 }
}