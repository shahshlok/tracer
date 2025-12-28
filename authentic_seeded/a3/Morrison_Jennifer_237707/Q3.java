import java.util.*;
public class Q3{
 public static void main(String[]args){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter text: ");
  String y=x.nextLine();
  String a=y;
  String b=a;
  a=a.toUpperCase();
  b=b.replace(' ','_');
  System.out.println("Result: "+y);
 }
}