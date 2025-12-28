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
  for(int i=0;i<n;i++){
   int a=i,b=t[i];String c=s[i];
   for(int j=i+1;j<n;j++)if(t[j]<b){b=t[j];c=s[j];a=j;}
   t[a]=t[i];s[a]=s[i];t[i]=b;s[i]=c;
  }
  System.out.println("Top student: "+s[n-1]+" ("+t[n-1]+")");
 }
}