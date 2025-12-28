import java.util.*;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter size: ");
int n=0;
if(x.hasNextInt())n=x.nextInt();
int[] y=new int[0];
if(n>0)y=new int[n];
System.out.print("Enter elements: ");
int i=0;
while(i<n){
if(x.hasNextInt())y[i]=x.nextInt();
i++;
}
System.out.print("Enter target: ");
int t=0;
if(x.hasNextInt())t=x.nextInt();
int k=-1;
i=0;
while(i<n){
int z=y[i];
if(z==t){
k=i;
i=n;
}else i++;
}
System.out.print("Found at index: ");
System.out.print(k);
}
}