

//Jiaqing Mo
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
public class BB{


   static int num_item;
   static int[] value;
   static int[] weight;
   static int capacity;
   static double[] vw;
   static int[] orig_id;
   static ArrayList<Integer> res;
   static Node max_node;
   public static void main(String[] args){
      long startTime = System.currentTimeMillis();
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
      sort_value();
      try{
         branch_bound();
      }catch(Exception el){
         if (max_node!=null) {
            res = new ArrayList<Integer>();
            for (int j = 0; j<num_item; j++){
               if (max_node.cur_res[j]==1)
                  res.add(orig_id[j]+1);
            }
            System.out.println("(out of memory) Using Branch and Bound the best feasible solution found: Value "+max_node.cur_val+", Weight "+max_node.cur_weight);
            Collections.sort(res);
            for (int j = 0; j< res.size(); j++){
               System.out.printf(res.get(j)+" ");
            }
            System.out.println("");
         }
      }
      
      long endTime = System.currentTimeMillis();
      System.out.println("Time: "+(endTime-startTime));
      return;
   }

static void sort_value(){
   orig_id = new int[num_item];//keep track of the original index
   vw = new double[num_item];
   double[] temp = new double[num_item];
  
   int i;
   for (i = 0; i<num_item; i++){
      temp[i] = (double)value[i]/weight[i];   //best criterion: larger value/weight first
   }
   for (i = 0; i<num_item; i++){//orig_id is the orignal index for sorted_ftime
      int max = getMax(temp);
      orig_id[i] = max;
      vw[i] = temp[max];
      temp[max] = -1;

   }
}
static int getMax(double[] vw){
   int max = 0, i = 0;
   while (vw[i]==-1.0){
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
   static class Node implements Comparable<Node>{
      int cur_val;
      int cur_weight;
      int[] cur_res;
      int item;
      double ub;
      public int compareTo(Node other){
         if (other.ub>this.ub) return 1;
         else if (this.ub > other.ub) return -1;
         else return 0;
      }
   }
   static double find_ub(Node n){
      if (n.item==num_item) return n.cur_val;
      return (capacity - n.cur_weight) * vw[n.item] + n.cur_val;
   }
   static void branch_bound(){
      PriorityQueue<Node> q = new PriorityQueue<Node>();
      Node n = new Node();
      n.cur_res = new int[num_item];
      for (int i = 0; i< num_item; i++){
         n.cur_res[i] = -1;
      }
      n.item = 0;
      n.cur_val = 0;
      n.cur_weight = 0;
      n.ub = find_ub(n);
      q.add(n);
      double lower_bound = 0;
      Node max_node = new Node();
      max_node.cur_val = 0;
      while(!q.isEmpty()){
         Node v = q.poll();
         if (v.ub>lower_bound && v.item <= num_item){
            //not take the item
            Node u = new Node();
            u.item = v.item + 1;
            u.cur_weight = v.cur_weight;
            u.cur_val = v.cur_val;
            u.cur_res = Arrays.copyOf(v.cur_res, num_item);
            u.cur_res[u.item-1] = 0;
            u.ub = find_ub(u);
             
            if (u.ub> max_node.cur_val && u.item<num_item) q.add(u);
            if (max_node.cur_val<u.cur_val){
               max_node = u;
            }
            if (u.item == num_item){
               //if (u.ub>lower_bound) 
                 // lower_bound = u.ub;
            }
            
            //same as the above but take the item
            u = new Node();
            u.item = v.item + 1;
            u.cur_weight = v.cur_weight + weight[orig_id[u.item-1]];
            if (u.cur_weight> capacity) continue;
            u.cur_val = v.cur_val + value[orig_id[u.item-1]];
            u.cur_res = Arrays.copyOf(v.cur_res, num_item);
            u.cur_res[u.item-1]=1;
            u.ub = find_ub(u);
            if (u.ub > max_node.cur_val && u.item<num_item) q.add(u); //parent
            if (u.cur_val>max_node.cur_val)
               max_node = u;
            if (u.item == num_item){
              // if (u.ub>lower_bound)//leaf
               //   lower_bound = u.ub;
            }
            
         }

      }
      res = new ArrayList<Integer>();
      for (int j = 0; j<num_item; j++){
         if (max_node.cur_res[j]==1)
            res.add(orig_id[j]+1);
      }
      System.out.println("Using Branch and Bound the best feasible solution found: Value "+max_node.cur_val+", Weight "+max_node.cur_weight);
      Collections.sort(res);
      for (int j = 0; j< res.size(); j++){
         System.out.printf(res.get(j)+" ");
      }
      System.out.println("");
      return;
   }
}
