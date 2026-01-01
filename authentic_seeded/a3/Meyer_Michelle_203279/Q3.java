import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter text: ");
String y=x.nextLine();
y=y.toUpperCase().replace(" ","_");
System.out.println("Result: "+y);
}
}