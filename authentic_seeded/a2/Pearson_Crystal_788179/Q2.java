import java.util.*;
public class Q2{
  public static void main(String[] args){
    Scanner x=new Scanner(System.in);
    Random y=new Random();
    int n=y.nextInt(100)+1;
    int a=0;
    int b=0;
    while(a!=n){
      System.out.print("Guess a number (1-100): ");
      a=x.nextInt();
      b++;
      if(a<n)System.out.println("Too low!");
      else if(a>n)System.out.println("Too high!");
    }
    System.out.println("Correct! You took "+b+" guesses.");
  }
}