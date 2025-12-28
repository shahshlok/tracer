import java.util.*;
public class Q1{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.print("Enter size: ");
int n=x.nextInt();
int[] y=new int[n];
System.out.print("Enter elements: ");
for(int i=0;i<n;i++)y[i]=x.nextInt();
System.out.print("Enter target: ");
int t=x.nextInt();
int a=-1;
for(int i=0;i<n;i++){
if(y[i]==t){
a=i;
break;
}
}
if(a==-1)System.out.println("-1");
else System.out.println("Found at index: "+a);
}
}