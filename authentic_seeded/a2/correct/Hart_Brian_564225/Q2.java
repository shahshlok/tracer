import java.util.*;
public class Q2{
public static void main(String[]a){
Random r=new Random();
int n=r.nextInt(100)+1;
Scanner s=new Scanner(System.in);
int x=0;
int y=0;
while(x!=n){
System.out.print("Guess a number (1-100): ");
x=s.nextInt();
y++;
if(x<n)System.out.println("Too low!");
else if(x>n)System.out.println("Too high!");
}
System.out.println("Correct! You took "+y+" guesses.");
}
}