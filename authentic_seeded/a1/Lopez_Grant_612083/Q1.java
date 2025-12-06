import java.util.Scanner;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
int v0=x.nextInt();
int v1=x.nextInt();
int t=x.nextInt();
int y=v1-v0;
double n=0;
if(t!=0){
int z=y/t;
n=z;
}
System.out.println("The average acceleration is "+n);
}
}