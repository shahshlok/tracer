import java.util.*;
public class Q1{
public static void main(String[] a){
Scanner s=new Scanner(System.in);
int n=0;
if(true)n=s.nextInt();
int[] x=new int[n];
int i=0;
while(i<n){
x[i]=s.nextInt();
i++;
}
int t=0;
if(true)t=s.nextInt();
int y=-1;
int j=0;
while(j<n){
int v=x[j];
if(v==t){
y=j;
j=n;
}else{
j++;
}
}
System.out.println(y);
}
}