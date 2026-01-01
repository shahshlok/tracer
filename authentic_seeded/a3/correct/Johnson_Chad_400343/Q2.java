import java.util.*;
public class Q2{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter number of students: ");
int n=x.nextInt();
String[] y=new String[n];
int[] s=new int[n];
System.out.print("Enter names: ");
for(int i=0;i<n;i++)y[i]=x.next();
System.out.print("Enter scores: ");
for(int i=0;i<n;i++)s[i]=x.nextInt();
for(int i=0;i<n-1;i++)for(int j=0;j<n-1-i;j++)if(s[j]>s[j+1]){int t=s[j];s[j]=s[j+1];s[j+1]=t;String u=y[j];y[j]=y[j+1];y[j+1]=u;}
System.out.println("Top student: "+y[n-1]+" ("+s[n-1]+")");
}
}