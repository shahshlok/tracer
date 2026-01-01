import java.util.*;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter text: ");
String y=x.nextLine();
y.toUpperCase();
y.replace(' ','_');
System.out.println("Result: "+y);
}
}