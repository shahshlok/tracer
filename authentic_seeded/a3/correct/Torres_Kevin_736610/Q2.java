import java.util.*;
public class Q2{
 public static void main(String[]a){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter number of students: ");
  int n=x.nextInt();
  String[] s=new String[n];
  int[] t=new int[n];
  System.out.print("Enter names: ");
  for(int i=0;i<n;i++)s[i]=x.next();
  System.out.print("Enter scores: ");
  for(int i=0;i<n;i++)t[i]=x.nextInt();
  for(int i=0;i<n-1;i++)for(int j=0;j<n-1-i;j++)if(t[j]>t[j+1]){int y=t[j];t[j]=t[j+1];t[j+1]=y;String z=s[j];s[j]=s[j+1];s[j+1]=z;}
  System.out.println("Top student: "+s[n-1]+" ("+t[n-1]+")");
 }
}