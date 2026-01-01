import java.util.*;
public class Q2{
 public static void main(String[]a){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter number of students: ");
  int n=x.nextInt();
  if(n<0)n=0;
  String[] y=new String[n];
  int[] z=new int[n];
  System.out.print("Enter names: ");
  int i=0;
  while(i<n){
   String t=x.next();
   y[i]=t;
   i++;
  }
  System.out.print("Enter scores: ");
  i=0;
  while(i<n){
   int t=x.nextInt();
   z[i]=t;
   i++;
  }
  i=0;
  while(i<n){
   int j=0;
   while(j<n-1){
    int s1=z[j];
    int s2=z[j+1];
    if(s1>s2){
     int tt=z[j];
     z[j]=z[j+1];
     z[j+1]=tt;
     String ts=y[j];
     y[j]=y[j+1];
     y[j+1]=ts;
    }
    j++;
   }
   i++;
  }
  if(n>0){
   int k=n-1;
   String ns=y[k];
   int sc=z[k];
   System.out.println("Top student: "+ns+" ("+sc+")");
  }
 }
}