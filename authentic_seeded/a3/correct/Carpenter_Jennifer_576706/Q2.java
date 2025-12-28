import java.util.*;
public class Q2{
 public static void main(String[]z){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter number of students: ");
  int n=x.nextInt();
  String[] s=new String[n];
  int[] t=new int[n];
  System.out.print("Enter names: ");
  for(int i=0;i<n;i++)s[i]=x.next();
  System.out.print("Enter scores: ");
  for(int i=0;i<n;i++)t[i]=x.nextInt();
  for(int i=0;i<n;i++)for(int j=0;j<n-1;j++){int a=t[j],b=t[j+1];if(a>b){t[j]=b;t[j+1]=a;String c=s[j];s[j]=s[j+1];s[j+1]=c;}}
  System.out.println("Top student: "+s[n-1]+" ("+t[n-1]+")");
 }
}