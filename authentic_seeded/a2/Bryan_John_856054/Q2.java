import java.util.*;
public class Q2{
    public static void main(String[]args){
        Random r=new Random();
        int a=r.nextInt(100)+1;
        Scanner s=new Scanner(System.in);
        int x=0;
        int y=0;
        while(x!=a){
            System.out.print("Guess a number (1-100): ");
            x=s.nextInt();
            y++;
            if(x>a)System.out.println("Too high!");
            else if(x<a)System.out.println("Too low!");
        }
        System.out.println("Correct! You took "+y+" guesses.");
    }
}