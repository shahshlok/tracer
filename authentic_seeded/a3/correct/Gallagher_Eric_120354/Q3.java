import java.util.*;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter text: ");
String y=x.nextLine();
if(y==null)y="";
String n=y.toUpperCase();
if(n==null)n="";
String z=n.replace(' ','_');
if(z==null)z="";
System.out.println("Result: "+z);
}
}