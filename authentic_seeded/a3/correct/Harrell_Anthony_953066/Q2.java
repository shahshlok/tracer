import java.util.*;
public class Q2{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter number of students: ");
int n=x.nextInt();
String[] y=new String[n];
int[] z=new int[n];
System.out.print("Enter names: ");
for(int i=0;i<n;i++)y[i]=x.next();
System.out.print("Enter scores: ");
for(int i=0;i<n;i++)z[i]=x.nextInt();
for(int i=0;i<n-1;i++)for(int j=0;j<n-1-i;j++)if(z[j]>z[j+1]){int t=z[j];z[j]=z[j+1];z[j+1]=t;String s=y[j];y[j]=y[j+1];y[j+1]=s;}
System.out.println("Top student: "+y[n-1]+" ("+z[n-1]+")");
}
}