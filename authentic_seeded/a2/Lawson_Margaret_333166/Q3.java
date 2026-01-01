import java.util.Scanner; 
public class Q3 { 
 public static void main(String[] args) { 
  Scanner x=new Scanner(System.in); 
  System.out.print("Enter grade: "); 
  int y=x.nextInt(); 
  String n; 
  if(y>=90)n="A"; 
  else if(y>=80)n="B"; 
  else if(y>=70)n="C"; 
  else if(y>=60)n="D"; 
  else n="F"; 
  System.out.println("Letter grade: "+n); 
 } 
}