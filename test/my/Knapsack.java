import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Knapsack {

   private static class Item implements Comparable<Item> {

      public int no;
      public double weight;
      public double value;
      public double unitValue;

      public int compareTo(Item other) {
         if (unitValue == other.unitValue)
            return 0;
         return (unitValue > other.unitValue)?-1:1;
      }

   }
   
   private static int n;
   private static double c;
   private static ArrayList<Item> items;

   // Branch-and-Bound solution

   private static class Node implements Comparable<Node> {

      public int level;
      public double currWeight;
      public double currValue;
      public double ub;

      public Node parent;
      public boolean taken;

      public int compareTo(Node other) {
         if (ub == other.ub)
            return 0;
         return (ub>other.ub)?-1:1;
      }

   }

   private static void bound(Node node) {
      double weight = c - node.currWeight;
      if (node.level+1 < n) {
         double nextUnitVal = items.get(node.level+1).unitValue;
         node.ub = node.currValue + weight * nextUnitVal;
      } else node.ub = node.currValue;
      if (weight < 0)
         node.ub = -1;
      node.level++;
   }

   private static void printNode(Node node) {
      System.out.printf("Node %d: w=%.1f v=%.1f ub=%.1f\n", node.level, node.currWeight, node.currValue, node.ub);
   }

   private static void traceback(Node node) {
      ArrayList<Integer> results = new ArrayList<Integer>();
      while (node.parent != null) {
         if (node.taken == true)
            results.add(node.level);
         node = node.parent;
      }
      for (int i=results.size()-1; i>=0; i--)
         System.out.print(results.get(i) + " ");
      System.out.println();
   }

   private static void knapsackBranchAndBound() {
      double max = -99999;
      Node root = new Node();
      Node last = null;
      root.currWeight = 0;
      root.currValue = 0;
      root.level = -1;
      bound(root);
      root.parent = null;
      root.taken = false;
      PriorityQueue<Node> queue = new PriorityQueue<Node>();
      queue.add(root);
      while (!queue.isEmpty()) {
         Node node = queue.poll();
         printNode(node);
         if (node.ub <= max)
            continue;
         if (node.level <= n) {
            // generate left
            Node left = new Node();
            left.level = node.level;
            left.currWeight = node.currWeight + items.get(left.level).weight;
            left.currValue = node.currValue + items.get(left.level).value;
            left.parent = node;
            left.taken = true;
            bound(left);

            // generate right
            Node right = new Node();
            right.level = node.level;
            right.currWeight = node.currWeight;
            right.currValue = node.currValue;
            right.parent = node;
            right.taken = false;
            bound(right);

            if (left.ub > max && left.ub > 0)
               queue.add(left);
            if (right.ub > max && right.ub > 0)
               queue.add(right);
            if ((left.ub < 0 && right.ub < 0) || node.level == n) {
               double val = node.currValue;
               if (val > max) {
                  max = val;
                  last = node;
               }
            }
         }
      }
      System.out.printf("Using Branch and Bound the best feasible solution found: value %.2f, Weight %.2f\n", max, last.currWeight);
      traceback(last);
   }

   public static void main(String[] args) {
      String filename = args[0];
      items = new ArrayList<Item>();
      Scanner sc = new Scanner(System.in);
      try {
         sc = new Scanner(new File(filename));
      } catch (Exception e) {
      }
      n = sc.nextInt();
      for (int i=0; i<n; i++) {
         int no = sc.nextInt();
         double value = sc.nextDouble();
         double weight = sc.nextDouble();
         Item it = new Item();
         it.no = no;
         it.value = value;
         it.weight = weight;
         it.unitValue = value/weight;
         items.add(it);
      }
      Item it = new Item();
      it.no = -1;
      it.value = 0;
      it.weight = 0;
      it.unitValue = 0;
      items.add(it);
      c = sc.nextInt();
      Collections.sort(items);
      knapsackBranchAndBound();
   }

}
