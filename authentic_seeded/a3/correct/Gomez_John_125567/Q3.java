import java.util.Scanner;
public class Q3{
public static void main(String[]x){
Scanner n=new Scanner(System.in);
System.out.print("Enter text: ");
String y="";
if(n.hasNextLine())y=n.nextLine();
String z=y;
if(z!=null)z=z.toUpperCase();
String a=z;
if(a!=null)a=a.replace(' ','_');
System.out.print("Result: ");
System.out.print(a);
}
}