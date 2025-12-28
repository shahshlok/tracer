import java.util.*;
public class Q4{
 public static void main(String[] a){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter size: ");
  int n=s.nextInt();
  int[] x=new int[n];
  System.out.print("Enter elements: ");
  int i=0;
  while(i<n){
   x[i]=s.nextInt();
   i++;
  }
  if(n>0){
   int y=x[n-1];
   int j=n-1;
   while(j>0){
    int t=x[j-1];
    x[j]=t;
    j--;
   }
   x[0]=y;
  }
  System.out.print("Shifted: ");
  int k=0;
  while(k<n){
   System.out.print(x[k]);
   if(k!=n-1){
    System.out.print(" ");
   }
   k++;
  }
 }
}