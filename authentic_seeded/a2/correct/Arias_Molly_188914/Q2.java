import java.util.*;
public class Q2{
  public static void main(String[]x){
    Random r=new Random();
    int a=r.nextInt(100)+1;
    Scanner s=new Scanner(System.in);
    int n=0;
    int y=0;
    while(y!=a){
      System.out.print("Guess a number (1-100): ");
      y=s.nextInt();
      n++;
      if(y<a)System.out.println("Too low!");
      else if(y>a)System.out.println("Too high!");
    }
    System.out.println("Correct! You took "+n+" guesses.");
  }
}