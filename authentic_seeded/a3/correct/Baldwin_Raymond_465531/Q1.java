import java.util.*;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter size: ");
int n=0;
if(x.hasNextInt())n=x.nextInt();
int[] y=new int[n];
System.out.print("Enter elements: ");
for(int i=0;i<n;i++){
int t=0;
if(x.hasNextInt())t=x.nextInt();
y[i]=t;
}
System.out.print("Enter target: ");
int z=0;
if(x.hasNextInt())z=x.nextInt();
int r=-1;
int i=0;
while(i<n){
int v=y[i];
if(v==z){
r=i;
i=n;
}else{
i++;
}
}
System.out.println("Found at index: "+r);
}
}