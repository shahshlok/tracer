import java.util.*;
public class Q3{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter text: ");
  String y=x.nextLine();
  String a=y.toUpperCase();
  String b=a.replace(' ','_');
  System.out.println("Result: "+b);
 }
}