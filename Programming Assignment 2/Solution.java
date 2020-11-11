import java.io.*;
import java.util.*;

public class Solution {

  public static void main(String[] args) throws IOException{
    TwoThreeTree tree = new TwoThreeTree();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String firstQuery = br.readLine();
    int numOfQue = Integer.parseInt(firstQuery);
    
    for(int i = 0; i < numOfQue; i++){
      String line = br.readLine();
      checkLine(line,tree);
    }
  }
    
  static void checkLine(String line, TwoThreeTree tree){
    if(line.charAt(0) == '1'){
      int indexOfSpace = line.indexOf(" ", 2);
      String planet = line.substring(2, indexOfSpace);
      int entranceFee = Integer.parseInt(line.substring(indexOfSpace+1));
      twothree.insert(planet, entranceFee, tree);
    }
    else if(line.charAt(0) == '2'){
      int firstSpace = line.indexOf(" ",2);
      int secondSpace = line.indexOf(" ",firstSpace+1);
      String firstSearch = line.substring(2,firstSpace);
      String secondSearch = line.substring(firstSpace+1,secondSpace);

      if(firstSearch.compareTo(secondSearch) > 0){
        String lowerBound = secondSearch;
        String upperBound = firstSearch;
        commonPath(lowerBound, upperBound, tree.root,Integer.parseInt(line.substring(secondSpace+1)), tree.height);
      }
      else
        commonPath(firstSearch, secondSearch, tree.root,Integer.parseInt(line.substring(secondSpace+1)), tree.height);
    }
    else if(line.charAt(0) == '3'){
      String searchFor = line.substring(2);
      System.out.println(searchAndPrint(searchFor, tree.root, tree.height,0));
    }
  }
    
  static String searchAndPrint(String possiblePlanet, Node root,int height,int total){
    if(height > 0){
      InternalNode internal = (InternalNode) root;

      if(possiblePlanet.compareTo(internal.child0.guide) <= 0)
        return searchAndPrint(possiblePlanet,internal.child0,height-1,total + internal.value);
      else if(possiblePlanet.compareTo(internal.child1.guide) <= 0)
        return searchAndPrint(possiblePlanet,internal.child1,height-1, total + internal.value);
      else{
        if(internal.child2 != null){
          if(possiblePlanet.compareTo(internal.child2.guide) <= 0)
            return searchAndPrint(possiblePlanet,internal.child2,height-1, total + internal.value);
          else
            return "-1";
        }
        else
          return "-1";
      }
    }
    else{
      String guide = "-1";
      if(root instanceof LeafNode){
        LeafNode leaf = (LeafNode) root;
        if(leaf.guide.compareTo(possiblePlanet) == 0)
          guide = (leaf.value+total)+"";
      }
      return guide;
    }
  }
    
    static void commonPath(String lowerBound, String upperBound, Node root, int value, int height){
      boolean nodesDiverging = false;
      if(height > 0){
        InternalNode internal = (InternalNode)root;

        if(internal.child2 == null){
          if(lowerBound.compareTo(internal.child0.guide) <= 0 && upperBound.compareTo(internal.child0.guide) <= 0){
            commonPath(lowerBound, upperBound, internal.child0, value, height-1);
          }
          else if(lowerBound.compareTo(internal.child1.guide) <= 0 && upperBound.compareTo(internal.child0.guide) > 0 && lowerBound.compareTo(internal.child0.guide) > 0){
            commonPath(lowerBound, upperBound, internal.child1, value, height-1);
          }
          else
            nodesDiverging = true;
        }
        else{
          if(lowerBound.compareTo(internal.child0.guide) <= 0 && upperBound.compareTo(internal.child0.guide) <= 0){
            commonPath(lowerBound, upperBound, internal.child0, value, height-1);
          }
          else if(lowerBound.compareTo(internal.child1.guide) <= 0 && upperBound.compareTo(internal.child2.guide) < 0 && (!(upperBound.compareTo(internal.child1.guide) > 0)) && lowerBound.compareTo(internal.child0.guide) > 0){
            commonPath(lowerBound, upperBound, internal.child1, value, height-1);
          }
          else if(lowerBound.compareTo(internal.child2.guide) <= 0 && upperBound.compareTo(internal.child1.guide) > 0 && lowerBound.compareTo(internal.child1.guide) > 0){
            commonPath(lowerBound, upperBound, internal.child2, value, height-1);
          }
          else
            nodesDiverging = true;
          }
        if (nodesDiverging) {
            if(internal.child2 != null) {
              if(lowerBound.compareTo(internal.child0.guide) <= 0 && upperBound.compareTo(internal.child1.guide) > 0){
                internal.child1.value += value;
                travelLeft(lowerBound, internal.child0,value, height-1);
                travelRight(upperBound, internal.child2, value, height-1);
              }
              else if(lowerBound.compareTo(internal.child0.guide) > 0 && upperBound.compareTo(internal.child1.guide) > 0){
                travelLeft(lowerBound,internal.child1,value,height-1);
                travelRight(upperBound,internal.child2,value,height-1);
              }
              else{
                travelLeft(lowerBound,internal.child0,value,height-1);
                travelRight(upperBound,internal.child1,value,height-1);
              }
            }
            else {
              if(lowerBound.compareTo(internal.child1.guide) < 0){
                travelLeft(lowerBound,internal.child0,value,height-1);
                travelRight(upperBound,internal.child1,value,height-1);
              }
            }
          }
      }
      else{
        if(lowerBound.compareTo(root.guide) <= 0 && upperBound.compareTo(root.guide) >= 0){
          root.value += value;
        }
      }
    }
    
  static void travelLeft(String lowerBound, Node leftPath, int value, int height){
    if(height > 0){
      InternalNode internal = (InternalNode) leftPath;

      if(lowerBound.compareTo(internal.child0.guide) <= 0){
        internal.child1.value += value;
        if(internal.child2 != null)
          internal.child2.value += value;
        travelLeft(lowerBound, internal.child0, value, height-1);
      }
      else if(lowerBound.compareTo(internal.child1.guide) <= 0){
        if(internal.child2 != null)
          internal.child2.value += value;
        travelLeft(lowerBound, internal.child1, value, height-1);
      }
      else{
              if(internal.child2 != null)
             travelLeft(lowerBound, internal.child2, value, height-1);
      }
    }
    else {
      if(lowerBound.compareTo(leftPath.guide) <= 0)
        leftPath.value += value;
    }
  }
    
  static void travelRight(String upperBound, Node rightPath, int value, int height){
    if(height > 0){
      InternalNode internal = (InternalNode) rightPath;
      
      if(internal.child2 != null){
        if(upperBound.compareTo(internal.child1.guide) > 0){
          internal.child0.value += value;
          internal.child1.value += value;
          travelRight(upperBound, internal.child2, value, height-1);
        }
              else if(upperBound.compareTo(internal.child0.guide) > 0){
                  internal.child0.value += value;
                  travelRight(upperBound, internal.child1, value, height -1);
              }
              else{
                  travelRight(upperBound, internal.child0, value, height-1);
              }
      }
      else{
        if(upperBound.compareTo(internal.child0.guide) > 0){
          internal.child0.value += value;
          travelRight(upperBound, internal.child1, value, height-1);
        }
        else{
          travelRight(upperBound, internal.child0, value, height-1);
        }
      }
    }
    else{
      if(upperBound.compareTo(rightPath.guide) >= 0)
        rightPath.value += value;
    }
  }
}

class twothree{

  // insert a key value pair into tree (overwrite existing value
  // if key is already present)
  static void insert(String key, int value, TwoThreeTree tree) {
    int h = tree.height;

    if (h == -1) {
      LeafNode newLeaf = new LeafNode();
      newLeaf.guide = key;
      newLeaf.value = value;
      tree.root = newLeaf; 
      tree.height = 0;
    }
    else{
      WorkSpace ws = doInsert(key, value, tree.root, h);

      if(ws != null && ws.newNode != null){
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
  static WorkSpace doInsert(String key, int value, Node p, int h){
    if(h == 0){
      // we're at the leaf level, so compare and 
      // either update value or insert new leaf

      LeafNode leaf = (LeafNode) p; //downcast
      int cmp = key.compareTo(leaf.guide);

      if (cmp == 0) {
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
      InternalNode q = (InternalNode) p; // downcast in if statements as well change value
      int pos;
      WorkSpace ws;

      if (key.compareTo(q.child0.guide) <= 0) {
        pos = 0;
        q.child0.value += q.value;
        q.child1.value += q.value;
        
        if(q.child2 != null)
          q.child2.value += q.value;
        
        q.value = 0;
        ws = doInsert(key, value, q.child0, h-1);
      }
      else if(key.compareTo(q.child1.guide) <= 0 || q.child2 == null){
        pos = 1;
        q.child0.value += q.value;
        q.child1.value += q.value;
        
        if(q.child2 != null)
          q.child2.value += q.value;
        
        q.value = 0;
        ws = doInsert(key, value, q.child1, h-1);
      }
      else{
        pos = 2; 
        q.child0.value += q.value;
        q.child1.value += q.value;
        q.child2.value += q.value;
        q.value = 0;
        ws = doInsert(key, value, q.child2, h-1);
      }

      if (ws != null) {
        if (ws.newNode != null) {
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

  // copy children of q into x, and return # of children change value in copy out children
  static int copyOutChildren(InternalNode q, Node[] x) {
    int sz = 2;
    x[0] = q.child0; x[1] = q.child1;
    q.child0.value += q.value;
    q.child1.value += q.value;
    
    if (q.child2 != null) {
      x[2] = q.child2;
      q.child2.value += q.value;
      sz = 3;
    }
    
    q.value = 0;
    return sz;
  }

  // insert p in x[0..sz) at position pos,
  // moving existing extries to the right
  static void insertNode(Node[] x, Node p, int sz, int pos){
    for (int i = sz; i > pos; i--)
      x[i] = x[i-1];

    x[pos] = p;
  }

  static boolean resetGuide(InternalNode q) {
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
  int value = 0;
}

class InternalNode extends Node {
  // child0 and child1 are always non-null
  // child2 is null iff node has only 2 children
  Node child0, child1, child2;
}

class LeafNode extends Node {
   // guide points to the key
}

class TwoThreeTree{
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