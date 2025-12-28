import java.util.*;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter size: ");
int n=x.nextInt();
int[] y=new int[n];
System.out.print("Enter elements: ");
for(int i=0;i<n;i++)y[i]=x.nextInt();
System.out.print("Enter target: ");
int t=x.nextInt();
int k=-1;
for(int i=0;i<n;i++){if(y[i]==t){k=i;break;}}
if(k==-1)System.out.print("-1");
else System.out.print("Found at index: "+k);
}
}