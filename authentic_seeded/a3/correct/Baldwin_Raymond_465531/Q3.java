import java.util.*;
public class Q3{
 public static void main(String[]a){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter text: ");
  String y="";
  if(x.hasNextLine())y=x.nextLine();
  String n=y.toUpperCase();
  if(n!=null)n=n.replace(" ","_");
  System.out.print("Result: ");
  if(n!=null)System.out.print(n);
 }
}