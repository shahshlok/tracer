import java.util.*;
public class Q3{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.print("Enter text: ");
String n=y.nextLine();
String a=n.toUpperCase();
String b=a.replace(" ","_");
System.out.println("Result: "+b);
}
}