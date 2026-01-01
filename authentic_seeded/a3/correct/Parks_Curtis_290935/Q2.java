import java.util.*;
public class Q2{
 public static void main(String[] a){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter number of students: ");
  int n=0;
  if(s.hasNextInt())n=s.nextInt();
  if(n<0)n=0;
  String[] x=new String[n];
  int[] y=new int[n];
  if(n>0)System.out.print("Enter names: ");
  int i=0;
  while(i<n){
   String t="";
   if(s.hasNext())t=s.next();
   x[i]=t;
   i++;
  }
  if(n>0)System.out.print("Enter scores: ");
  i=0;
  while(i<n){
   int t=0;
   if(s.hasNextInt())t=s.nextInt();
   y[i]=t;
   i++;
  }
  i=0;
  while(i<n){
   int j=i+1;
   while(j<n){
    int t1=y[i];
    int t2=y[j];
    if(t2<t1){
     int th=y[i];
     y[i]=y[j];
     y[j]=th;
     String ts=x[i];
     x[i]=x[j];
     x[j]=ts;
    }
    j++;
   }
   i++;
  }
  String rName="";
  int rScore=0;
  if(n>0){
   rName=x[n-1];
   rScore=y[n-1];
  }
  if(n>0)System.out.println("Top student: "+rName+" ("+rScore+")");
 }
}