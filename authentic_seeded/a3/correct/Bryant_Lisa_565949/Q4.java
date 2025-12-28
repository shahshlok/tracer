import java.util.*;
public class Q4{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
int n=0;
System.out.print("Enter size: ");
if(x.hasNextInt())n=x.nextInt();
if(n<0)n=0;
int[] y=new int[n];
System.out.print("Enter elements: ");
int i=0;
while(i<n){
if(x.hasNextInt()){
int t=x.nextInt();
y[i]=t;
}
i++;
}
System.out.print("Shifted: ");
if(n>0){
int t=y[n-1];
int j=n-1;
while(j>0){
int u=y[j-1];
y[j]=u;
j--;
}
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