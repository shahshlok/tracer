import java.util.*;
public class Q1{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.print("Enter size: ");
int n=x.nextInt();
int[] y=new int[n];
System.out.print("Enter elements: ");
int i=1;
while(i<=n){y[i]=x.nextInt();i++;}
System.out.print("Enter target: ");
int t=x.nextInt();
int a=-1,b=1,c=n;
while(b<=c&&a==-1){if(y[b]==t)a=b;b++;}
System.out.print("Found at index: ");
System.out.println(a);
}
}