import java.util.*;
public class Q2{
 public static void main(String[]x){
  Random r=new Random();
  int a=r.nextInt(100)+1;
  int b=0;
  int c=0;
  Scanner s=new Scanner(System.in);
  while(b!=a){
   System.out.print("Guess a number (1-100): ");
   b=s.nextInt();
   c++;
   if(b<a)System.out.println("Too low!");
   else if(b>a)System.out.println("Too high!");
  }
  System.out.println("Correct! You took "+c+" guesses.");
 }
}