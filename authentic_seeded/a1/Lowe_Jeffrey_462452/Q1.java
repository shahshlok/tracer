import java.util.Scanner;
public class Q1 {
public static void main(String[] args) {
Scanner x=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
double v0=0;double v1=0;double t=0;
x.nextDouble();x.nextDouble();x.nextDouble();
System.out.println("The average acceleration is "+((v1-v0)/t));
}
}