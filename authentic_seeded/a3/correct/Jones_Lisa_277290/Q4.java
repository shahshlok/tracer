import java.util.*;
public class Q4{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.print("Enter size: ");
int n=s.nextInt();
int[] x=new int[n];
System.out.print("Enter elements: ");
for(int i=0;i<n;i++)x[i]=s.nextInt();
if(n>0){
int y=x[n-1];
for(int i=n-1;i>0;i--)x[i]=x[i-1];
x[0]=y;
}
System.out.print("Shifted: ");
for(int i=0;i<n;i++){
System.out.print(x[i]);
if(i<n-1)System.out.print(" ");
}
}
}