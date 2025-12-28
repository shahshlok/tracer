import java.util.Scanner;
public class Q3{
 public static void main(String[]a){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter text: ");
  String x=s.nextLine();
  String y="";
  if(x!=null)y=x.toUpperCase();
  String n="";
  int i=0;
  if(y!=null){
   while(i<y.length()){
    char c=y.charAt(i);
    char d=c;
    if(c==' ')d='_';
    n=n+d;
    i++;
   }
  }
  System.out.println("Result: "+n);
 }
}