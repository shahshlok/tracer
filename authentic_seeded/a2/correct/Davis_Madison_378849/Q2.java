import java.util.*;
public class Q2{
public static void main(String[]a){
Random r=new Random();
int x=r.nextInt(100)+1;
Scanner s=new Scanner(System.in);
int y=0;
int n=0;
while(y!=x){
System.out.print("Guess a number (1-100): ");
y=s.nextInt();
n++;
if(y<x)System.out.println("Too low!");
else if(y>x)System.out.println("Too high!");
}
System.out.println("Correct! You took "+n+" guesses.");
}
}