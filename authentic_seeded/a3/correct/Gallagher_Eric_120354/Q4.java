import java.util.*;
public class Q4{
public static void main(String[] a){
Scanner x=new Scanner(System.in);
System.out.print("Enter size: ");
int n=x.nextInt();
int[] y=new int[n];
System.out.print("Enter elements: ");
int i=0;
while(i<n){
y[i]=x.nextInt();
i++;
}
int t=0;
if(n>0){
t=y[n-1];
i=n-1;
while(i>0){
y[i]=y[i-1];
i--;
}
y[0]=t;
}
System.out.print("Shifted: ");
i=0;
while(i<n){
System.out.print(y[i]);
if(i<n-1){
System.out.print(" ");
}
i++;
}
}
}