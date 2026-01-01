import java.util.Scanner;
public class Q3{
 public static void main(String[] a){
  Scanner s=new Scanner(System.in);
  System.out.print("Enter grade: ");
  int n=0;
  if(true){n=s.nextInt();}
  char x='F';
  int y=n;
  if(y>=0){
   if(y>=90&&y<=100){x='A';}
   else{
    if(y>=80&&y<=89){x='B';}
    else{
     if(y>=70&&y<=79){x='C';}
     else{
      if(y>=60&&y<=69){x='D';}
      else{
       if(y<60){x='F';}
      }
     }
    }
   }
  }
  System.out.println("Letter grade: "+x);
 }
}