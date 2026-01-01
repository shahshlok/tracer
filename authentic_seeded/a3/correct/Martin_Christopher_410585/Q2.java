import java.util.*;
public class Q2{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter number of students: ");
  int n=s.nextInt();
  String[] y=new String[n];
  int[] z=new int[n];
  System.out.print("Enter names: ");
  for(int i=0;i<n;i++)y[i]=s.next();
  System.out.print("Enter scores: ");
  for(int i=0;i<n;i++)z[i]=s.nextInt();
  for(int i=0;i<n-1;i++)for(int j=0;j<n-1-i;j++){int a=z[j],b=z[j+1];if(a>b){z[j]=b;z[j+1]=a;String c=y[j];y[j]=y[j+1];y[j+1]=c;}}
  System.out.println("Top student: "+y[n-1]+" ("+z[n-1]+")");
 }
}