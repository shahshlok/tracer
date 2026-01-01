import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.print("Enter text: ");
String x=s.nextLine();
if(x==null)x="";
String y=x.toUpperCase();
if(y==null)y="";
String n=y.replace(" ","_");
if(n==null)n="";
System.out.println("Result: "+n);
}
}