import java.util.*;
public class Q4{
public static void main(String[]x){
Scanner s=new Scanner(System.in);
System.out.print("Enter size: ");
int n=s.nextInt();
int[] a=new int[n];
System.out.print("Enter elements: ");
for(int i=0;i<n;i++)a[i]=s.nextInt();
if(n>0){
int b=a[n-1];
for(int i=n-1;i>0;i--)a[i]=a[i-1];
a[0]=b;
}
System.out.print("Shifted: ");
for(int i=0;i<n;i++){
System.out.print(a[i]);
if(i<n-1)System.out.print(" ");
}
}
}