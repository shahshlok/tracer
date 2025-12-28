import java.util.*;
public class Q3{
 public static void main(String[]a){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter text: ");
  String y="";
  if(x!=null)y=x.nextLine();
  if(y==null)y="";
  String n=y;
  if(n!=null)n.toUpperCase();
  if(n!=null)n.replace(' ','_');
  System.out.print("Result: ");
  if(n!=null)System.out.print(n);
 }
}