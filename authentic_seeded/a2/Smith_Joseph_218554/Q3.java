import java.util.*;
public class Q3{
  public static void main(String[]x){
    Scanner y=new Scanner(System.in);
    System.out.print("Enter grade: ");
    int n=y.nextInt();
    int a=90,b=80,c=70,d=60;
    String s=n>=a?"A":n>=b?"B":n>=c?"C":n>=d?"D":"F";
    System.out.println("Letter grade: "+s);
  }
}