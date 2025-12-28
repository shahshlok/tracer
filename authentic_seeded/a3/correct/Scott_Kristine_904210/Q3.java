import java.util.*;
public class Q3{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter text: ");
  String y=s.nextLine();
  String a=y.toUpperCase();
  String b=a.replace(" ","_");
  System.out.println("Result: "+b);
 }
}