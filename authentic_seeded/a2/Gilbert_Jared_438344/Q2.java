import java.util.*;
public class Q2{
 public static void main(String[]a){
  Random r=new Random();
  int x=r.nextInt(100)+1;
  int y=0;
  int n=0;
  Scanner s=new Scanner(System.in);
  boolean z=false;
  while(!z){
   System.out.print("Guess a number (1-100): ");
   if(s.hasNextInt()){
    y=s.nextInt();
    n=n+1;
    if(y==x){
     z=true;
    }else{
     if(y>x){
      System.out.println("Too high!");
     }else{
      System.out.println("Too low!");
     }
    }
   }else{
    String t=s.next();
    if(t!=null){
     t=t;
    }
   }
  }
  if(n!=0){
   System.out.println("Correct! You took "+n+" guesses.");
  }
 }
}