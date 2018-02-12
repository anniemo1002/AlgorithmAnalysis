
//Jiaqing Mo
//CPE 349
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
public class Greedy{ 

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
      value = new int[num_item];
      weight = new int[num_item];
      int index;
      for (int i = 0; i<num_item; i++){
         index = sc.nextInt();
         value[index-1] = sc.nextInt();
         weight[index-1] = sc.nextInt();
      }
      capacity = sc.nextInt();
      getOptSet();
      return;
   }
static double[] getOptSet(){
   int[] orig_id = new int[num_item];//keep track of the original index
   double [] vw = new double[num_item];
  
   int i;
   for (i = 0; i<num_item; i++){
      vw[i] = (double)value[i]/weight[i];   //best criterion: larger value/weight first
   }
   for (i = 0; i<num_item; i++){//orig_id is the orignal index for sorted_ftime
      int max = getMax(vw);
      orig_id[i] = max;
      vw[max] = -1.0;
   }
   //System.out.println(Arrays.toString(orig_id));
   int cur_val = 0;
   int cur_weight = 0;
   ArrayList<Integer> res = new ArrayList<Integer>();
   for (i = 0; i<num_item;i++){
      
      if (weight[orig_id[i]]+cur_weight<=capacity){
         cur_weight+=weight[orig_id[i]];
         cur_val+=value[orig_id[i]];
         res.add(orig_id[i]);
      }
   }
   System.out.println("Greedy solution (not necessarily optimal): Value "+cur_val+", Weight "+cur_weight);
   Collections.sort(res);
   for (int e : res){
      System.out.print(e+1 +" ");
   }
   System.out.println("");
   return vw;
}
/*static int[] TVW(int[] stime, int[] sorted_ftime, int[] weight, int length, int[] orig_id, int[] prev){
   int[] res = new int[length];
   if (length==1){
      res[0] = weight[orig_id[0]];
      prev[0] = -1;
      return res;
   }
   int[] temp = TVW(stime, sorted_ftime, weight, length-1, orig_id, prev);
   int max = weight[orig_id[length-1]];
   int prev_id = -1;
   int i;
   for (i = 0; i<length-1; i++){
      if (stime[orig_id[length-1]]>=sorted_ftime[i]){
         int temp_wgt = temp[i]+weight[orig_id[length-1]];
         if (max<temp_wgt){
            prev_id = i;
            max = temp_wgt;
         }
      }
   }
   prev[length-1] = prev_id;
   for (i = 0; i<length-1; i++){
      res[i] = temp[i];
   }
   res[length-1] = max;
   return res;
}
static int Max(int[] arr){
   int i;
   int max = 0;
   for (i = 1; i<arr.length; i++){
      if (arr[max]<arr[i]) max = i;
   }
   return max;
}*/
static int getMax(double[] vw){
   int max = 0, i = 0;
   while (vw[i]==-1){
      i++;
   }
   max = i;
   int first = max;
   for(i = first; i<num_item; i++){
      if (vw[i]!=-1.0 && vw[max]<vw[i]){
         max = i;
      }
   }
   return max;
}

}
