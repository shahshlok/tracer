import java.util.*;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter text: ");
String y="";
if(x!=null)y=x.nextLine();
String n="";
if(y!=null)n=y.toUpperCase();
String r="";
if(n!=null)r=n.replace(' ','_');
System.out.println("Result: "+r);
}
}