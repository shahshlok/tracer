import java.util.*;
public class Q1{
 public static void main(String[]x){
  Scanner n=new Scanner(System.in);
  System.out.print("Enter size: ");
  int a=n.nextInt();
  int[]b=new int[a];
  System.out.print("Enter elements: ");
  for(int c=0;c<a;c++)b[c]=n.nextInt();
  System.out.print("Enter target: ");
  int d=n.nextInt();
  int e=-1;
  for(int c=0;c<a&&e==-1;c++)if(b[c]==d)e=c;
  if(e==-1)System.out.println("-1");else System.out.println("Found at index: "+e);
 }
}