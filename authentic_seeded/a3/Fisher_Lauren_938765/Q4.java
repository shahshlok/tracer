import java.util.*;
public class Q4{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter size: ");
  int n=s.nextInt();
  if(n<0)n=0;
  int[] a=new int[n];
  System.out.print("Enter elements: ");
  int i=1;
  while(i<=n){
   if(i<=n){
    a[i-1]=s.nextInt();
   }
   i++;
  }
  System.out.print("Shifted: ");
  if(n!=0){
   int t=a[n];
   int j=n;
   while(j>1){
    int h=a[j-2];
    if(j-1<n){
     a[j-1]=h;
    }
    j--;
   }
   if(n!=0){
    a[0]=t;
   }
  }
  int k=1;
  while(k<=n){
   System.out.print(a[k-1]);
   if(k!=n)System.out.print(" ");
   k++;
  }
 }
}