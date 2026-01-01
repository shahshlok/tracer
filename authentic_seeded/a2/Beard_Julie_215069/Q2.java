import java.util.*;
public class Q2{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
Random y=new Random();
int n=y.nextInt(100)+1;
int g=0;
int c=0;
while(g!=n){
System.out.print("Guess a number (1-100): ");
g=x.nextInt();
c++;
if(g<n)System.out.println("Too low!");
else if(g>n)System.out.println("Too high!");
}
System.out.println("Correct! You took "+c+" guesses.");
}
}