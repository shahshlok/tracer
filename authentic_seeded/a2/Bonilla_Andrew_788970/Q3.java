import java.util.Scanner;
public class Q3 {
    public static void main(String[] args) {
        Scanner x=new Scanner(System.in);
        System.out.print("Enter grade: ");
        int y=x.nextInt();
        if(y>=90)System.out.println("Letter grade: A");
        if(y>=80)System.out.println("Letter grade: B");
        if(y>=70)System.out.println("Letter grade: C");
        if(y>=60)System.out.println("Letter grade: D");
        if(y<60)System.out.println("Letter grade: F");
    }
}