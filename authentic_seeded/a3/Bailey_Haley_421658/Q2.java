import java.util.*;
public class Q2{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter number of students: ");
  int n=x.nextInt();
  String[] y=new String[n];
  int[] z=new int[n];
  System.out.print("Enter names: ");
  for(int i=0;i<n;i++)y[i]=x.next();
  System.out.print("Enter scores: ");
  for(int i=0;i<n;i++)z[i]=x.nextInt();
  Arrays.sort(z);
  System.out.println("Top student: "+y[n-1]+" ("+z[n-1]+")");
 }
}