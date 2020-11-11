import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

  public static void main(String[] args) throws IOException {
    TwoThreeTree testTree = new TwoThreeTree();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String line1 = br.readLine();
    int sizeOfDB = Integer.parseInt(line1);
    
    for(int i = 0; i<sizeOfDB;i++){
        String line = br.readLine();
        String[] info= line.split("\\s");
        twothree.insert(info[0], Integer.parseInt(info[1]),testTree);
    }
    
    String line2 = br.readLine();
    int sizeOfQuery = Integer.parseInt(line2);
    
    for(int i = 0; i< sizeOfQuery; i++){
      String line = br.readLine();
      String[] splitPlanetNames = new String[2];
      splitPlanetNames = line.split("\\s");

      if(splitPlanetNames[0].compareTo(splitPlanetNames[1]) > 0){
        String temp = splitPlanetNames[0];
        splitPlanetNames[0] = splitPlanetNames[1];
        splitPlanetNames[1] = temp;
      }
        searchAndPrint(splitPlanetNames[0],splitPlanetNames[1],testTree.root);
    }
  }

  public static void searchAndPrint(String lowerBound, String upperBound, Node root){
    if(root == null)
      return;

    InternalNode internal = (InternalNode) root;
    if(internal.child0 instanceof LeafNode){
      LeafNode lf0 = (LeafNode) internal.child0;
      LeafNode lf1 = (LeafNode) internal.child1;
     
      if(lf0.guide.compareTo(lowerBound) >= 0 && lf0.guide.compareTo(upperBound) <= 0)
        System.out.println(lf0.guide+" "+lf0.value);
      if(lf1.guide.compareTo(lowerBound) >= 0 && lf1.guide.compareTo(upperBound) <= 0)
        System.out.println(lf1.guide+" "+lf1.value);
      if(internal.child2 != null){
        LeafNode lf2 = (LeafNode) internal.child2;
        if(lf2.guide.compareTo(lowerBound) >= 0 && lf2.guide.compareTo(upperBound) <= 0)
          System.out.println(lf2.guide+" "+lf2.value);
      }
    }
    else{
     searchAndPrint(lowerBound, upperBound, internal.child0);
     searchAndPrint(lowerBound, upperBound, internal.child1);
     searchAndPrint(lowerBound, upperBound, internal.child2);
    }
  }
}

class twothree {

  static void insert(String key, int value, TwoThreeTree tree) {
    int h = tree.height;

    if(h == -1){
        LeafNode newLeaf = new LeafNode();
        newLeaf.guide = key;
        newLeaf.value = value;
        tree.root = newLeaf; 
        tree.height = 0;
    }
    else{
      WorkSpace ws = doInsert(key, value, tree.root, h);

      if (ws != null && ws.newNode != null) {
        // create a new root
        InternalNode newRoot = new InternalNode();
        if (ws.offset == 0) {
          newRoot.child0 = ws.newNode; 
          newRoot.child1 = tree.root;
        }
        else {
          newRoot.child0 = tree.root; 
          newRoot.child1 = ws.newNode;
        }
        resetGuide(newRoot);
        tree.root = newRoot;
        tree.height = h+1;
      }
    }
  }
  
  // auxiliary recursive routine for insert
  static WorkSpace doInsert(String key, int value, Node p, int h) {
    if(h == 0){
      // we're at the leaf level, so compare and 
      // either update value or insert new leaf
      LeafNode leaf = (LeafNode) p; //downcast
      int cmp = key.compareTo(leaf.guide);

      if(cmp == 0){
        leaf.value = value; 
        return null;
      }

      // create new leaf node and insert into tree
      LeafNode newLeaf = new LeafNode();
      newLeaf.guide = key; 
      newLeaf.value = value;

      int offset = (cmp < 0) ? 0 : 1;
      // offset == 0 => newLeaf inserted as left sibling
      // offset == 1 => newLeaf inserted as right sibling

      WorkSpace ws = new WorkSpace();
      ws.newNode = newLeaf;
      ws.offset = offset;
      ws.scratch = new Node[4];

      return ws;
    }
    else{
      InternalNode q = (InternalNode) p; // downcast
      int pos;
      WorkSpace ws;

      if(key.compareTo(q.child0.guide) <= 0){
        pos = 0; 
        ws = doInsert(key, value, q.child0, h-1);
      }
      else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
        pos = 1;
        ws = doInsert(key, value, q.child1, h-1);
      }
      else{
        pos = 2; 
        ws = doInsert(key, value, q.child2, h-1);
      }

      if(ws != null){
        if(ws.newNode != null){
          // make ws.newNode child # pos + ws.offset of q
          int sz = copyOutChildren(q, ws.scratch);
          insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
          if (sz == 2) {
            ws.newNode = null;
            ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
          }
          else{
            ws.newNode = new InternalNode();
            ws.offset = 1;
            resetChildren(q, ws.scratch, 0, 2);
            resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
          }
        }
        else if(ws.guideChanged){
          ws.guideChanged = resetGuide(q);
        }
      }

      return ws;
    }
  }

  // copy children of q into x, and return # of children
  static int copyOutChildren(InternalNode q, Node[] x) {
    int sz = 2;
    x[0] = q.child0; x[1] = q.child1;
    
    if (q.child2 != null) {
      x[2] = q.child2; 
      sz = 3;
    }
    return sz;
  }

  // insert p in x[0..sz) at position pos,
  // moving existing extries to the right
  static void insertNode(Node[] x, Node p, int sz, int pos) {
    for (int i = sz; i > pos; i--)
      x[i] = x[i-1];

    x[pos] = p;
  }

  static boolean resetGuide(InternalNode q){
    String oldGuide = q.guide;
    if (q.child2 != null)
      q.guide = q.child2.guide;
    else
      q.guide = q.child1.guide;

    return q.guide != oldGuide;
  }

  // reset q's children to x[pos..pos+sz), where sz is 2 or 3.
  // also resets guide, and returns the result of that
  static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
    q.child0 = x[pos]; 
    q.child1 = x[pos+1];

    if (sz == 3) 
      q.child2 = x[pos+2];
    else
      q.child2 = null;

    return resetGuide(q);
  }

}

class Node {
  // guide points to max key in subtree rooted at node
  String guide;
}

class InternalNode extends Node {
  // child0 and child1 are always non-null
  // child2 is null iff node has only 2 children
  Node child0, child1, child2;
}

class LeafNode extends Node {
  // guide points to the key
  int value;
}

class TwoThreeTree {
  Node root;
  int height;

  TwoThreeTree() {
    root = null;
    height = -1;
  }
}

// this class is used to hold return values for the recursive doInsert
// routine
class WorkSpace {
  Node newNode;
  int offset;
  boolean guideChanged;
  Node[] scratch;
}