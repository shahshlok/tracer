import java.util.Scanner;
public class Q1{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter v0, v1, and t: ");
  int v0=x.nextInt();
  int v1=x.nextInt();
  int t=x.nextInt();
  double a,b,c;
  a=(double)((v1-v0)/t);
  b=a;
  c=b;
  System.out.println("The average acceleration is "+c);
 }
}