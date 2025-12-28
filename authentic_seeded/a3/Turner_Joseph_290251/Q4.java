import java.util.*;
public class Q4{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.print("Enter size: ");
int n=x.nextInt();
int[] y=new int[n];
System.out.print("Enter elements: ");
for(int i=0;i<n;i++)y[i]=x.nextInt();
if(n>0){
int a=y[n-1];
for(int i=n-1;i>0;i--)y[i]=y[i-1];
y[0]=a;
}
System.out.print("Shifted: ");
for(int i=0;i<n;i++){
System.out.print(y[i]);
if(i<n-1)System.out.print(" ");
}
}
}