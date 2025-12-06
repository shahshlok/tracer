import java.util.Scanner;
public class Q1{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
int v0=y.nextInt();
int v1=y.nextInt();
int t=y.nextInt();
double n=(double)((v1-v0)/t);
System.out.println("The average acceleration is "+n);
}
}