import java.util.*;
public class Q1{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.print("Enter size: ");
int n=y.nextInt();
int[]a=new int[n];
System.out.print("Enter elements: ");
for(int i=0;i<n;i++)a[i]=y.nextInt();
System.out.print("Enter target: ");
int b=y.nextInt();
int c=-1;
for(int i=0;i<n;i++)if(a[i]==b){c=i;break;}
if(c==-1)System.out.println("-1");
else System.out.println("Found at index: "+c);
}
}