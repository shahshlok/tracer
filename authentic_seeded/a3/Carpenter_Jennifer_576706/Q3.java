import java.util.*;
public class Q3{
 public static void main(String[]x){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter text: ");
  String n=s.nextLine();
  String a=n;
  a.toUpperCase();
  a.replace(" ","_");
  System.out.println("Result: "+a);
 }
}