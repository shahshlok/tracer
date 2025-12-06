import java.util.*;
public class Main{
public static void main(String[]args){
Scanner s=new Scanner(System.in);
System.out.print("Enter v0, v1, and t: ");
double v0=s.nextDouble(),v1=s.nextDouble(),t=s.nextDouble();
System.out.println("The average acceleration is "+(v1-v0)/t);
}
}
