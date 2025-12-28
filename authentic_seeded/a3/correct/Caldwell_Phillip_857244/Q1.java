import java.util.*;
public class Q1{
public static void main(String[]x){
Scanner s=new Scanner(System.in);
System.out.print("Enter size: ");
int n=s.nextInt();
int[] a=new int[n];
System.out.print("Enter elements: ");
for(int i=0;i<n;i++)a[i]=s.nextInt();
System.out.print("Enter target: ");
int t=s.nextInt();
int y=-1;
for(int i=0;i<n;i++)if(a[i]==t){y=i;break;}
if(y==-1)System.out.println("-1");
else System.out.println("Found at index: "+y);
}
}