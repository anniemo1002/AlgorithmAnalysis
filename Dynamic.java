
//Jiaqing Mo
//CPE 349
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
public class Dynamic{ 
   static int num_item;
   static int[] value;
   static int[] weight;
   static int capacity;
   static int total_weight = 0;
   static int[][] table;
   static ArrayList<Integer> res = new ArrayList<Integer>();
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
      value = new int[num_item];
      weight = new int[num_item];
      int index;
      for (int i = 0; i<num_item; i++){
         index = sc.nextInt();
         value[index-1] = sc.nextInt();
         weight[index-1] = sc.nextInt();
      }
      capacity = sc.nextInt();
      knapsack();
      trace_back(num_item, capacity);
      System.out.println("Dynamic Programming solution: Value "+table[num_item][capacity]+", Weight "+ total_weight);
      Collections.sort(res);
      for (int i = 0; i<res.size(); i++){
         System.out.printf(res.get(i)+" ");
      }
      System.out.println("");
      return;
   }
   static int max(int a, int b){
      if (a>b) return a;
      else return b;
   }
   static int knapsack(){
      table = new int[num_item+1][capacity+1];
      for (int i = 0; i<=num_item; i++){
         for (int w = 0; w<=capacity; w++){
            if (i == 0 || w == 0){
               table[i][w] = 0;
            }
            else if (weight[i-1]<=w){
               table[i][w] = max(table[i-1][w-weight[i-1]]+value[i-1], table[i-1][w]);
            }
            else{
               table[i][w] = table[i-1][w];
            }
         }
      }
      return table[num_item][capacity];
   }
   static void trace_back(int i, int w){
      if (i==0||w==0) return;
      else if(weight[i-1] > w) trace_back(i-1, w);  //not taken
      else if (table[i-1][w-weight[i-1]]+value[i-1]> table[i-1][w]){ //taken
         res.add(i);
         total_weight+=weight[i-1];
         trace_back(i-1, w-weight[i-1]);
      }
      else trace_back(i-1,w);
   }

}
