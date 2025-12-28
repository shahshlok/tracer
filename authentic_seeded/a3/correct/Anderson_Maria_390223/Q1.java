import java.util.*;
public class Q1{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter size: ");
  int n=x.nextInt();
  int[] a=new int[n];
  System.out.print("Enter elements: ");
  for(int i=0;i<n;i++)a[i]=x.nextInt();
  System.out.print("Enter target: ");
  int t=x.nextInt();
  int y=-1;
  for(int i=0;i<n;i++)if(a[i]==t){y=i;break;}
  if(y==-1)System.out.print("-1");
  else System.out.print("Found at index: "+y);
 }
}