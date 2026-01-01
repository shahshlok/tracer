import java.util.*;
public class Q4{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter size: ");
  int n=s.nextInt();
  if(n<0)n=0;
  int[] a=new int[n];
  System.out.print("Enter elements: ");
  int i=0;
  while(i<n){
   a[i]=s.nextInt();
   i++;
  }
  System.out.print("Shifted: ");
  if(n!=0){
   int t=a[n-1];
   int j=n-1;
   while(j>0){
    int h=a[j-1];
    a[j]=h;
    j--;
   }
   a[0]=t;
  }
  int k=0;
  while(k<n){
   System.out.print(a[k]);
   if(k!=n-1)System.out.print(" ");
   k++;
  }
 }
}