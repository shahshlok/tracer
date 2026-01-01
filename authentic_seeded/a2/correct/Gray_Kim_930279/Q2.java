import java.util.*;
public class Q2{
 public static void main(String[]args){
  Random r=new Random();
  int a=100,b=1,c=r.nextInt(a)+b,x=0,y=0,n=0;
  Scanner s=new Scanner(System.in);
  while(n==0){
   System.out.print("Guess a number (1-100): ");
   x=s.nextInt();
   y++;
   if(x<c)System.out.println("Too low!");
   else if(x>c)System.out.println("Too high!");
   else n=1;
  }
  System.out.println("Correct! You took "+y+" guesses.");
 }
}