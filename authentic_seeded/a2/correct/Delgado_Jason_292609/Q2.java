import java.util.*;
public class Q2{
 public static void main(String[]args){
  Random r=new Random();
  int a=r.nextInt(100)+1;
  Scanner s=new Scanner(System.in);
  int x=0;
  int n=0;
  while(x!=a){
   System.out.print("Guess a number (1-100): ");
   x=s.nextInt();
   n++;
   if(x<a)System.out.println("Too low!");
   else if(x>a)System.out.println("Too high!");
  }
  System.out.println("Correct! You took "+n+" guesses.");
 }
}