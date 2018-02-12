import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;
public class Full_enum{
   static int num_item;
   static int[] value;
   static int[] weight;
   static int capacity;
   static ArrayList<int[]> bin_string = new ArrayList<int[]>();
   static int[] temp;
   public static void main(String[] args){
      if (args.length!=1){
         System.err.println("command not valid");
         return;
      }
      Scanner sc;
      try{
         sc = new Scanner(new File(args[0]));
      }catch(Exception e){
         System.err.println("file not found");
         return;
      }
      num_item = sc.nextInt();
      value = new int[num_item+1];
      weight = new int[num_item+1];
      int index;
      for (int i = 1; i<=num_item; i++){
         index = sc.nextInt();
         value[index] = sc.nextInt();
         weight[index] = sc.nextInt();
      }
      capacity = sc.nextInt();
      full_enum();
      return;
   }
   static void full_enum(){
      temp = new int[num_item];
      binary(num_item);
      /*for (int[] arr : bin_string){
         System.out.println(Arrays.toString(arr));
      }*/

      int max_val = 0;
      int max_weight = 0;
      int max_index = -1;
      int temp_val = 0;
      int temp_weight = 0;
      int i;
      for (i = 0; i<bin_string.size(); i++){
         temp_val = 0;
         temp_weight = 0;
         for (int j = 1; j<=num_item;j++){
            if (bin_string.get(i)[j-1]==0) continue;
            temp_val +=value[j];
            temp_weight +=weight[j];
         }
         if (temp_weight>capacity) continue;
         if (temp_val > max_val){
            max_val = temp_val;
            max_index = i;
            max_weight = temp_weight;
         }
      }
      System.out.println("Using Brute force best feasible solution found: Value "+max_val+", Weight "+max_weight);
      for (i = 0; i<num_item; i++){
         if (bin_string.get(max_index)[i]!=0){
            System.out.printf("%d ", i+1);
         }
      }
         System.out.println("");
      return;
   }
   static void binary(int n){
      if (n<1){
         bin_string.add(temp.clone());
      }
      else{
         temp[n-1] = 0;
         binary(n-1);
         temp[n-1] = 1;
         binary(n-1);
      }
      return;
   }
}
