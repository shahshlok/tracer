import java.util.*;
public class Q4{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter size: ");
int n=x.nextInt();
int[] y=new int[n];
System.out.print("Enter elements: ");
int i=0;
while(i<n){y[i]=x.nextInt();i++;}
System.out.print("Shifted: ");
if(n>0){
int t=y[n-1];
int j=n-1;
while(j>0){y[j]=y[j-1];j--;}
y[0]=t;
}
int k=0;
while(k<n){
System.out.print(y[k]);
if(k!=n-1)System.out.print(" ");
k++;
}
}
}