import java.util.*;
public class Q2{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.print("Enter number of students: ");
int n=x.nextInt();
String[] a=new String[n];
int[] b=new int[n];
System.out.print("Enter names: ");
for(int i=0;i<n;i++)a[i]=x.next();
System.out.print("Enter scores: ");
for(int i=0;i<n;i++)b[i]=x.nextInt();
for(int i=0;i<n-1;i++)for(int j=0;j<n-1-i;j++)if(b[j]>b[j+1]){int y=b[j];b[j]=b[j+1];b[j+1]=y;String s=a[j];a[j]=a[j+1];a[j+1]=s;}
System.out.println("Top student: "+a[n-1]+" ("+b[n-1]+")");
}
}